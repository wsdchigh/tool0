package com.wsdc.p_j_0.looper;

/*
 *  轮询器
 *  <li>    轮询器，本质属于死循环，需要在一定的时间内休眠，这样可以避免超过CPU占用，但是却没有做任何事情
 *  <li>    这里会自动开启一个子线程去执行轮询操作
 *
 *
 *  轮询器可能会出现的问题
 *  <li>    如果任务列表中没有可以执行的任务，或者说任务非常简单，那么就会出现空转，此时处理的速度非常快，直接导致CPU100%
 *  <li>    如果有任务，那么就不会这么离谱
 *
 *  <li>    如果是空转，那么间隔时间在1ms左右，是会略微影响cpu，在android上面是1% 不同的CPU结果可能不一样
 *
 *
 *  轮询器是一个非阻塞无限循环的线程
 *  <li>    没有阻塞是不可能的
 *  <li>    不要执行明显的阻塞操作
 */
public interface Looper {
    /*
     *  注册
     */
    void register(LCall lc);

    /*
     *  取消注册
     */
    void unregister(LCall lc);

    //  开始工作
    void work();

    //  退出  不会立即退出，一定会让当前的轮询执行完
    void exit();
}
