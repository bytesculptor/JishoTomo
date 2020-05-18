package net.emojiparty.android.jishotomo.data.di;

import net.emojiparty.android.jishotomo.data.AppRepository;
import net.emojiparty.android.jishotomo.data.room.EntryDao;
import net.emojiparty.android.jishotomo.data.room.SenseDao;

import javax.inject.Singleton;

import dagger.Component;

// https://medium.com/@marco_cattaneo/integrate-dagger-2-with-room-persistence-library-in-few-lines-abf48328eaeb
@Singleton
@Component(dependencies = {}, modules = {RoomModule.class})
public interface AppComponent {
    void inject(AppRepository appRepository);

    EntryDao entryDao();

    SenseDao senseDao();
}
