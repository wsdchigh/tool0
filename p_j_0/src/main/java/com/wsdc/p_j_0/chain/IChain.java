package com.wsdc.p_j_0.chain;

import java.util.List;

/*
 *  事件调用链
 *  <li>    任何任务均维系一个key，通常是Integer(不分大小的)
 *
 *  <li>    线程观念
 *          <li>    所有任务均在一个独立的线程中执行,这个线程是自己独立创建的，或者多个Chain共享一个默认的
 *          <li>    提供一个post函数，这个函数用于数据不同线程的切换
 *
 *  <li>    任务观念
 *          <li>    任务执行，要么成功，要么失败
 *          <li>    任务可以执行一次或者多次
 *
 *  <li>    关系表
 *          <li>    单链关系，如果A执行完毕，那么B执行
 *          <li>    多条件关系，如果A执行成功，并且B执行成功，我们才执行C
 *          <li>    或关系     如果A或者B执行成功，我们在执行C
 *          <li>    其他的复杂关系暂时不设定()
 *
 *  <li>    取消  (移除)
 *          <li>    为了避免内存泄漏，允许任务在任务表中移除
 *          <li>    线程中的任务只会执行一次
 *
 *  <li>    周期性
 *          <li>    同样是针对内存问题，提供周期性函数
 *          <li>    任务链应该和组件是同步周期的，里面的元素通常也是同步周期的
 *                  <li>    在周期之内，一直保存所有的任务(如果有特殊的需要，可以手动去移除)
 *                  <li>    可以调用remove函数执行移除
 *
 *  <li>    泛型
 *          <li>    K   key 任务标识自身的key(建议是Integer，其次是String)
 *          <li>    D   data    这里可以是任意类型，建议是一个容器(Map是最好的),用于存储过程中产生的数据
 *                  <li>    例如，如果这个任务执行成功了，那么将数据存放在Map表中，其他任务通过依赖任务的key取出数据
 *                          实现数据的流通
 *                  <li>    这是系统设计的初衷，对于数据的传递，可以通过其他方式，不一定要遵循系统的设定
 *
 *
 *  <li>    原则
 *          <li>    任务默认是多次执行
 *          <li>    如果任务只执行一次，那么执行的时候移除任务   (如果执行多次，那么不移除即可)
 */
public interface IChain<K,D> {

    IChain<K,D> doMain0(ITask<K, D> task);

    /*
     *  重载函数
     *  <li>    task本身就是任务，任务执行完毕之后的k，rtn是我们判断的依据
     *  <li>    任务复杂多样，可能存在着不同程度的阻塞
     *  如果我们需要线程就有高效的IO，那么使用下面的函数
     *  将阻塞的任务移动到阻塞线程之中，或者使用NIO的方式去处理，当结果处理完之后，调用这个函数发布结果
     *  <li>    同理doThen如果需要非阻塞，task可以返回一个没有注册的值，将阻塞的任务移动到其他线程
     *          直到任务处理完之后，调用doMain0函数发布结果
     *
     *  <li>    原则上面来说，涉及到同步，这个函数还是会有一定的阻塞的
     *  <li>    如果任务池已经处于停滞状态，需要调用这个函数唤醒
     */
    IChain<K,D> doMain0(K k);
    IChain<K,D> doThen(ITask<K, D> task);
    IChain<K,D> remove(ITask<K, D> task);

    IChain<K,D> remove(K k);
    IChain<K,D> remove(List<K> ks);

    IChain<K,D> removeAll();

    void start();

    void exit();

    /*
     *  任务完成之后，会发送一个信号到信号池中
     */
    void dispatch(ITask<K, D> task);

    /*
     *  任何任务执行完毕，均会轮询一次，找到激活的任务
     */
    void loop();

    /*
     *  通过key，检索到指定的任务
     */
    ITask<K,D> getTaskByKey(K k);

    D wrap();
}
