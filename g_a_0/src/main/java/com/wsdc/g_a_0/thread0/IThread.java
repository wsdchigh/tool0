package com.wsdc.g_a_0.thread0;

import java.util.Collection;

/*
 *  业务线程
 *  <li>    阻塞线程，如果需要不阻塞线程，使用Looper
 */
public interface IThread<A> {
    /*
     *  将任务添加到队头
     */
    void doFirst(A a);

    /*
     *  将任务添加到队尾
     */
    void doLast(A a);

    /*
     *  将所有的任务依次添加到队尾
     */
    void doAll(Collection<A> c);

    /*
     *  线程退出
     *  <li>    因为线程是阻塞的
     *  <li>    设置退出变量
     *  <li>    打断线程阻塞  (抛出打断异常)
     */
    void exit();

    /*
     *  主要执行线程
     */
    void run0(A a);
}
