package vip.com.vipmeetings.dagger;

import javax.inject.Singleton;

import dagger.Component;
import vip.com.vipmeetings.MainBaseActivity;
import vip.com.vipmeetings.VIPmeetings;
import vip.com.vipmeetings.service.PushService;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(VIPmeetings viPmeetings);
    void inject(MainBaseActivity viPmeetings);
   // void inject(PushService pushService);
}
