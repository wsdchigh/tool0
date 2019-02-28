package com.wsdc.g_a_0.chain;

import com.wsdc.g_a_0.thread0.AbstractWorkTread;
import com.wsdc.g_a_0.thread0.IThread;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 *  为了减少主线程的压力
 *  <li>    尽可能的将任务移动到IO线程中去执行
 */
public abstract class AbstractIChain implements IChain<Integer,Map<Integer,Object>> {
    /*
     *  任务池
     *  <li>    存放依赖任务，默认不会移除
     *  <li>    如果需要移除，需要手动调用函数去移除
     */
    private List<ITask<Integer,Map<Integer,Object>>> tasks = new LinkedList<>();

    /*
     *  存放消息队列
     *  <li>    任何任务处理完之后，所有的结果均存放在该结果里面
     *  <li>    在执行新的任务之前，会分发所有的信息
     */
    private Queue<Sem> sems = new LinkedList<>();
    private Lock lock = new ReentrantLock();

    private Map<Integer,Object> objectMap = new TreeMap<Integer,Object>();
    /*
     *  通常是一个组件对应一个任务池
     *  <li>    通常是有多个组件存放在栈中，所以，为每一个组件去创建一个线程是不可以去的
     *  <li>    设定一个公共的线程去执行任务
     *  <li>    如果有特殊的需要，可以去创建自己的独有线程
     *          <li>    通常公共插件是需要这样做的
     */
    public static IThread<ITask<Integer,Map<Integer,Object>>> innerThread = new AbstractWorkTread<ITask<Integer,Map<Integer,Object>>>() {

        @Override
        public void run0(ITask<Integer,Map<Integer,Object>> task) {
            /*
             *  轮到任务执行的时候
             *  <li>    此处为IO线程
             */
            IChain<Integer, Map<Integer, Object>> chain = task.getIChain();
            if(task.runInMain()){
                chain.post(task,chain);
            }else{
                int rtn = -1;
                try {
                    rtn = task.execute(chain.wrap());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                chain.dispatch(task.getTaskKey(),rtn);
            }
            chain.loop();
        }
    };

    public IThread<ITask<Integer,Map<Integer,Object>>> t1;

    public AbstractIChain() {
        this(new TreeMap<>());
    }

    public AbstractIChain(Map<Integer,Object> map) {
        this(innerThread,map);
    }

    public AbstractIChain(IThread<ITask<Integer,Map<Integer,Object>>> t1, Map<Integer,Object> map) {
        this.t1 = t1;
        this.objectMap = map;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> doMain0(ITask<Integer, Map<Integer, Object>> task) {
        task.setIChain(this);
        t1.doFirst(task);
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> doThen(ITask<Integer, Map<Integer, Object>> task) {
        lock.lock();
        task.setIChain(this);
        tasks.add(task);
        lock.unlock();
        //System.out.println("then tasks size = "+tasks.size());
        return this;
    }

    @Override
    public IChain<Integer, Map<Integer, Object>> remove(ITask<Integer, Map<Integer, Object>> task) {
        lock.lock();
        task.setIChain(this);
        tasks.remove(task);
        lock.unlock();



        return this;
    }

    @Override
    public void start() {

    }

    @Override
    public void exit() {
        if(t1 != innerThread){
            t1.exit();
        }

        tasks.clear();
    }

    @Override
    public void dispatch(Integer integer, int rtn) {
        lock.lock();
        Sem sem0 = new Sem(integer,rtn);
        sems.add(sem0);
        //System.out.println("dispatch sems size = "+sems.size());
        lock.unlock();
    }



    @Override
    public void loop() {
        lock.lock();
        try{
            Sem sem0 = null;

            final List<ITask<Integer,Map<Integer,Object>>> tmpList = new LinkedList<>();
            final List<ITask<Integer,Map<Integer,Object>>> tmpRemoveList = new LinkedList<>();

            //System.out.println("loop tasks size = "+tasks.size()+"  sem size = "+sems.size());
            while((sem0 = sems.poll()) != null){
                for (ITask<Integer, Map<Integer,Object>> task : tasks) {
                    task.receive(sem0.key,sem0.rtn);
                    if(task.shouldExecute()){
                        tmpList.add(task);
                        if(!task.isMulti()){
                            tmpRemoveList.add(task);
                        }
                    }
                }

            }

            t1.doAll(tmpList);
            tasks.removeAll(tmpRemoveList);
            //System.out.println("size = "+tmpList.size());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Object> wrap() {
        return objectMap;
    }

    private static class Sem{
        public Sem() {
        }

        public Sem(Integer key, int rtn) {

            this.key = key;
            this.rtn = rtn;
        }

        Integer key;
        int rtn;
    }
}