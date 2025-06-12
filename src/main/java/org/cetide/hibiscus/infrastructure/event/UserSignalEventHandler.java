package org.cetide.hibiscus.infrastructure.event;

import com.hibiscus.signal.Signals;
import com.hibiscus.signal.core.SignalContext;
import com.hibiscus.signal.spring.anno.SignalHandler;
import org.cetide.hibiscus.common.notification.SendNotifyStrategy;
import org.cetide.hibiscus.common.notification.impl.SendNotificationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class UserSignalEventHandler {

    private final SendNotifyStrategy sendNotifyStrategy;
    private final SendNotificationImpl sendNotificationService;

    private static final Logger log = LoggerFactory.getLogger(UserSignalEventHandler.class);
    private final Signals signals;

    public UserSignalEventHandler(@Qualifier("emailStrategy") SendNotifyStrategy sendNotifyStrategy, SendNotificationImpl sendNotificationService, Signals signals) {
        this.sendNotifyStrategy = sendNotifyStrategy;
        this.sendNotificationService = sendNotificationService;
        this.signals = signals;
    }


    @SignalHandler(value = "user.login", target = UserSignalEventHandler.class, methodName = "handleLogin")
    public void handleLogin(SignalContext signalContext) throws Exception {
        Map<String, Object> requestParams = signalContext.getAttributes();
        if (requestParams != null){
            for (Map.Entry<String, Object> params : requestParams.entrySet()){
                System.out.println("键: "+params.getKey() + "  值:" + params.getValue());
            }
        }
        String result = (String) signalContext.getAttributes().get("result");
        System.out.println("函数结果: "+result);
    }
}
