package com.java.thread;

public class TestCorrectPostureStep1 {

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (room){
                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(!hasCigarette){
                    System.out.println("【小南】没有烟，先歇会");
                    try {
                        Thread.sleep(2000);//sleep不释放锁，一直阻塞在这里，别人也拿不到只能等待
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(hasCigarette){
                    System.out.println("【小南】可以开始干活了");
                }

            }
        },"小南").start();

        for(int i = 0; i<5; i++){
            new Thread(()->{
                synchronized (room){
                    System.out.println("【其他人】可以开始干活了");
                }
            },"其他人").start();
        }

        Thread.sleep(1000);
        new Thread(()->{
            //synchronized (room)  送烟的时候不能枷锁，因为小南持有锁，拿不到锁，进不去，
            hasCigarette = true;
            System.out.println("【送烟的】烟到了");
        },"送烟的").start();
    }
}

class TestCorrectPostureStep2 {

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (room){
                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(!hasCigarette){
                    System.out.println("【小南】没有烟，先歇会");
                    try {
                        room.wait();//释放对象锁，别人可以拿到锁，可以继续干活
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(hasCigarette){
                    System.out.println("【小南】可以开始干活了");
                }

            }
        },"小南").start();

        for(int i = 0; i<5; i++){
            new Thread(()->{
                synchronized (room){
                    System.out.println("【其他人】可以开始干活了");
                }
            },"其他人").start();
        }

        Thread.sleep(1000);
        new Thread(()->{
            synchronized (room){
                hasCigarette = true;
                System.out.println("【送烟的】烟到了");
                room.notify();
            }

        },"送烟的").start();
    }
}


class TestCorrectPostureStep3 {

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (room){
                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(!hasCigarette){
                    System.out.println("【小南】没有烟，先歇会");
                    try {
                        room.wait();//释放对象锁，别人可以拿到锁，可以继续干活
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(hasCigarette){
                    System.out.println("【小南】可以开始干活了");
                }else{
                    System.out.println("【小南】没干活");
                }

            }
        },"小南").start();

        new Thread(()->{
            synchronized (room){
                System.out.println("【小女】外卖到了没？ "+hasCigarette);
                if(!hasTakeout){
                    System.out.println("【小女】没外卖，先歇会");
                    try {
                        room.wait();//释放对象锁，别人可以拿到锁，可以继续干活
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小女】外卖到了没？ "+hasTakeout);
                if(hasTakeout){
                    System.out.println("【小女】可以开始干活了");
                }else{
                    System.out.println("【小女】没干活");
                }

            }
        },"小女").start();


        Thread.sleep(1000);
        new Thread(()->{
            synchronized (room){
                hasTakeout = true;
                System.out.println("【送外卖的】外卖到了");
                room.notifyAll();
            }

        },"送外卖的").start();
    }
}


class TestCorrectPostureStep4 {

    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized (room){
                System.out.println("【小南】有烟吗？ "+hasCigarette);
                //while为true时，里面的代码块会再次执行不会退出循环，不会执行while代码块后面的代码。if条件被唤醒后从上次停止的地方往下执行，所以不会再次进入等待
                while (!hasCigarette){
                    System.out.println("【小南】没有烟，先歇会");
                    try {
                        room.wait();//释放对象锁，别人可以拿到锁，可以继续干活
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小南】有烟吗？ "+hasCigarette);
                if(hasCigarette){
                    System.out.println("【小南】可以开始干活了");
                }else{
                    System.out.println("【小南】没干活");
                }

            }
        },"小南").start();

        new Thread(()->{
            synchronized (room){
                System.out.println("【小女】外卖到了没？ "+hasCigarette);
                if(!hasTakeout){
                    System.out.println("【小女】没外卖，先歇会");
                    try {
                        room.wait();//释放对象锁，别人可以拿到锁，可以继续干活
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("【小女】外卖到了没？ "+hasTakeout);
                if(hasTakeout){
                    System.out.println("【小女】可以开始干活了");
                }else{
                    System.out.println("【小女】没干活");
                }

            }
        },"小女").start();


        Thread.sleep(1000);
        new Thread(()->{
            synchronized (room){
                hasTakeout = true;
                System.out.println("【送外卖的】外卖到了");
                room.notifyAll();
            }

        },"送外卖的").start();
    }
}