package home.saied.samples;

import androidx.annotation.NonNull;
import androidx.compose.runtime.RecomposeScope;
import androidx.compose.runtime.RecomposeScopeImpl;
import androidx.compose.runtime.collection.IdentityArrayIntMap;

import org.apache.commons.lang3.reflect.FieldUtils;

class ReadObserver {
    static Object[] observations(@NonNull RecomposeScope recomposeScope) throws IllegalAccessException {
        IdentityArrayIntMap identityArrayIntMap = (IdentityArrayIntMap) FieldUtils.getField(RecomposeScopeImpl.class, "trackedInstances", true).get(recomposeScope);
        if (identityArrayIntMap == null)
            return new Object[] {};
        return identityArrayIntMap.getKeys();
    }
}
