package internal.di;

import net.Endpoints;

import dagger.Component;

@Component(modules = DataModule.class)
public interface  DataComponent {
    Endpoints endpoints();
}
