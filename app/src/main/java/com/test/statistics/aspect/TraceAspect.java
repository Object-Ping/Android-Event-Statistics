package com.test.statistics.aspect;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 截获类名最后含有Activity、Layout的类的所有方法
 * 监听目标方法的执行时间
 */
@Aspect
public class TraceAspect {
//  private static Object currentObject = null;

    private static final String POINTCUT_METHOD =
            "(execution(* *..Activity+.*(..))||execution(* *..Fragment+.*(..))" +
                    "||execution(public void onClick(android.view.View))" +
                    "||execution(public void doClick(android.view.View)))&& target(Object) && this(Object)";
//      "(execution(* *..Activity+.*(..)) ||execution(* *..Layout+.*(..)))&& target(Object) && this(Object)";

    private static final String POINTCUT_METHOD_MAINACTIVITY = "execution(* *..MainActivity+.onCreate(..))";
    //精确截获MyFrameLayou的onMeasure方法
    private static final String POINTCUT_CALL = "call(* org.android10.viewgroupperformance.component.MyFrameLayout.onMeasure(..))";

    //    private static final String POINTCUT_CALL = "call(* android.view.View.OnClickListener(..))";
    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotated() {
    }

    @Pointcut(POINTCUT_METHOD_MAINACTIVITY)
    public void methodAnootatedWith() {
    }

    @Pointcut(POINTCUT_CALL)
    public void methodAnootatedCall() {
    }

    /**
     * 截获原方法，并替换
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("methodAnnotated()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {

//    if (currentObject == null){
//        currentObject = joinPoint.getTarget();
//    }
        StringBuffer buffer = new StringBuffer();
        //初始化计时器
        final StopWatch stopWatch = new StopWatch();
        //开始监听
        stopWatch.start();
        //调用原方法的执行。
        Object result = joinPoint.proceed();
        //监听结束
        stopWatch.stop();
        //获取方法信息对象
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //获取当前方法
        Method method = methodSignature.getMethod();

        //获取当前参数名称
        String[] parameterNames = methodSignature.getParameterNames();
        //获取当前参数类型
        Class[] parameterTypes = methodSignature.getParameterTypes();
        //获取当前返回值类型
        Class returnType = methodSignature.getReturnType();
        //获取当前对象，通过反射获取类别详细信息
        String className = joinPoint.getThis().getClass().getName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();

        if (args != null && args.length > 0) {
            buffer.append("(");
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    // TODO: 获取参数信息
                    buffer.append(parameterTypes[i].getSimpleName() + " " + parameterNames[i] + (i == args.length - 1 ? "" : ","));
                }
            }
            buffer.append(")");
            buffer.append(" --> ");
            buffer.append("[");
            buffer.append(stopWatch.getTotalTime(1));
            buffer.append(StopWatch.Accuracy == 1 ? "ms" : "mic");
            buffer.append("]      \n");
            Log.e(className, Modifier.toString(methodSignature.getModifiers()) + " " + returnType.getName() + " " + methodName + buffer.toString());
        }

        String msg = buildLogMessage(methodName, stopWatch.getTotalTime(1));
//    if (currentObject != null && currentObject.equals(joinPoint.getTarget())){
//        DebugLog.log(new MethodMsg(className,msg, (long) stopWatch.getTotalTime(1)));
//    }else if(currentObject != null && !currentObject.equals(joinPoint.getTarget())){
        DebugLog.log(new MethodMsg(className, msg, (long) stopWatch.getTotalTime(1)));
//        Log.e(className, msg);
//        currentObject = joinPoint.getTarget();
//        DebugLog.outPut(new Path());    //日志存储
//        DebugLog.ReadIn(new Path());    //日志读取
//    }
        return result;
    }

//    @After("methodAnootatedWith()")
//    public void onCreateAfter(JoinPoint joinPoint) throws Throwable{
//        Log.e("onCreateAfter:","onCreate is end .");
//    }
//    /**
//     * 在截获的目标方法调用之前执行该Advise
//     * @param joinPoint
//     * @throws Throwable
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    @Before("methodAnootatedWith()")
//    public void onCreateBefore(JoinPoint joinPoint) throws Throwable{
//        Activity activity = null;
//        //获取目标对象
//        activity = ((Activity)joinPoint.getTarget());
//        //插入自己的实现，控制目标对象的执行
////        ChooseDialog dialog = new ChooseDialog(activity);
////        dialog.show();
//
//        //做其他的操作
//        buildLogMessage("test",20);
//    }

    /**
     * 创建一个日志信息
     *
     * @param methodName     方法名
     * @param methodDuration 执行时间
     * @return
     */
    private static String buildLogMessage(String methodName, double methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(methodName);
        message.append(" --> ");
        message.append("[");
        message.append(methodDuration);
        if (StopWatch.Accuracy == 1) {
            message.append("ms");
        } else {
            message.append("mic");
        }
        message.append("]      \n");

        return message.toString();
    }

}
