package iskallia.vault.util.nbt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NBTSerialize {

    String name() default "";

    Class<?> typeOverride() default Object.class;
}
