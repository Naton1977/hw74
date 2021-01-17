package org.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.domain.entity.ValueAspectCached;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class CachedAspect {

    private final Map<ValueAspectCached, Object> valueAspectCachedPostMap = new ConcurrentHashMap<>();


    @Pointcut("@annotation(org.example.aspect.Cached)")
    private void cachedPointCut() {
    }

    @Around("cachedPointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        Object retVal;
        ValueAspectCached valueAspectCached = new ValueAspectCached(jp.getSignature().getName(), jp.getSignature().getName(), Arrays.toString(jp.getArgs()));

        boolean presentKey = valueAspectCachedPostMap.containsKey(valueAspectCached);

        if (presentKey) {
            return valueAspectCachedPostMap.get(valueAspectCached);

        } else {
            retVal = jp.proceed();
            valueAspectCachedPostMap.put(valueAspectCached, retVal);
        }

        return retVal;
    }

}
