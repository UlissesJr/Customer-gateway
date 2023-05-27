package top.luobogan.completeable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by LuoboGan
 * Date:2023-05-26
 */

/**
 * public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,
 * BiFunction<? super T,? super U,? extends V> fn)
 * 其中，`other`是另一个`CompletionStage`对象，
 * `fn`是一个接受两个参数的函数，用于将两个阶段的结果合并为一个新的结果。
 * `thenCombine`方法返回一个新的`CompletionStage`对象，用于表示两个阶段的结果合并后的结果。
 * 下面是一个示例，假设有两个`CompletionStage`对象`stage1`和`stage2`，它们的结果分别是`result1`和`result2`。
 * 可以使用`thenCombine`方法将它们的结果合并为一个新的结果：
 * CompletionStage<String> stage1 = CompletableFuture.supplyAsync(() -> "Hello");
 * CompletionStage<String> stage2 = CompletableFuture.supplyAsync(() -> "world");
 * CompletionStage<String> combinedStage = stage1.thenCombine(stage2, (s1, s2) -> s1 + " " + s2);
 * combinedStage.thenAccept(System.out::println);
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        // 任务1 洗水壶 -> 烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1 洗水壶......");
            sleep(1,TimeUnit.SECONDS);

            System.out.println("T1 烧开水.....");
            sleep(15,TimeUnit.SECONDS);

        } );

        // 任务2 洗水壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(()->{
            System.out.println("T2 洗水壶......");
            sleep(1,TimeUnit.SECONDS);

            System.out.println("T2 洗茶杯.....");
            sleep(2,TimeUnit.SECONDS);

            System.out.println("T2 拿茶叶.....");
            sleep(3,TimeUnit.SECONDS);

            return "红茶";
        });

        // 任务3 任务1 和 任务2 完成后执行： 泡茶
        CompletableFuture<String> f3 = f1.thenCombine(f2,(v,res) -> {
            System.out.println("T3 拿到茶叶....." + res);
            System.out.println("T3 泡茶");
            return "喝茶" + res;
        });

        // 等待任务3 的执行结果
        System.out.println(f3.join());

    }

    static void sleep(int t , TimeUnit u){
        try{
            u.sleep(t);
        }catch(InterruptedException e){

        }

    }

}
