package pl.norbit.treecuter.config;

import net.j4c0b3y.api.config.ConfigHandler;
import net.j4c0b3y.api.config.StaticConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.nio.file.Path;

public class SettingsExtra extends StaticConfig {


    @Ignore
    public static SettingsExtra INSTANCE;

    @Ignore
    public static final ConfigHandler HANDLER = new ConfigHandler();

    public SettingsExtra(Path directory) {
        super(directory.resolve("config-extra.yml").toFile(), HANDLER);
        INSTANCE = this;
    }

    public static class GENERAL {

        public static boolean USE_ENCHANTMENT = true;

        public static int ASYNC_THREADS = 2;

    }

    public static class ENCHANTMENT {

        public static String DESCRIPTION = "";
        @Ignore
        public static Component _DESCRIPTION;

        public static int ANVIL_COST = 2;

        public static int MAX_LEVEL = 5;

        public static int WEIGHT = 10;

        public static class MINIMUM_COST {

            public static int BASE_COST = 2;

            public static int ADDITIONAL_PER_LEVEL = 1;

        }

        public static class MAXIMUM_COST {

            public static int BASE_COST = 4;

            public static int ADDITIONAL_PER_LEVEL = 2;

        }

        public static class BEHAVIOUR {

            public static int BASE_BLOCKS_BREAK = 10;

            public static int EXTRA_BLOCKS_BREAK_PER_LEVEL = 60;

        }

    }

    @Override
    public void load() {
        super.load();

        ENCHANTMENT._DESCRIPTION = MiniMessage.miniMessage().deserialize(ENCHANTMENT.DESCRIPTION);
    }

}
