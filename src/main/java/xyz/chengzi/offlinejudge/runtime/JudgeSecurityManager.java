package xyz.chengzi.offlinejudge.runtime;

import java.security.Permission;
import java.util.PropertyPermission;

public class JudgeSecurityManager extends SecurityManager {
    private volatile boolean locked = true;
    private final long unlockCode;

    JudgeSecurityManager(long unlockCode) {
        this.unlockCode = unlockCode;
    }

    @Override
    public void checkPermission(Permission perm) {
        if (!locked && perm.getName().equals("setSecurityManager")) {
            return;
        }
        if (perm instanceof RuntimePermission && (perm.getName().equals("getClassLoader") || perm.getName()
                .equals("localeServiceProvider") || perm.getName().equals("setIO"))) {
            return;
        }
        if (perm instanceof PropertyPermission) {
            return;
        }
        if (perm.getName().equals("suppressAccessChecks")) {
            return;
        }
        super.checkPermission(perm);
    }

    void unlock(long unlockCode) {
        if (this.unlockCode == unlockCode) {
            locked = false;
            return;
        }
        throw new SecurityException();
    }
}
