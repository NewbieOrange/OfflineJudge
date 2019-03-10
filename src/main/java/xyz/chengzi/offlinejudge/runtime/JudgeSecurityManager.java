package xyz.chengzi.offlinejudge.runtime;

import xyz.chengzi.offlinejudge.OfflineJudge;

import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.Arrays;
import java.util.PropertyPermission;

public class JudgeSecurityManager extends SecurityManager {
    private volatile boolean locked = true;
    private static ThreadGroup rootGroup;
    private final long unlockCode;

    JudgeSecurityManager(long unlockCode) {
        this.unlockCode = unlockCode;
    }

    @Override
    public void checkPermission(Permission perm) {
        if (!locked && perm.getName().equals("setSecurityManager")) {
            return;
        }
        if (perm instanceof RuntimePermission) {
            switch (perm.getName()) {
                case "getClassLoader":
                case "localeServiceProvider":
                case "setIO":
                    return;
                case "accessDeclaredMembers":
                    if (isSystemClass(getClassContext()[3])) {
                        return;
                    }
                    break;
                case "stopThread":
                    if (getClassContext()[2] == Judge.class) {
                        return;
                    }
                    break;
            }
        }
        if (perm instanceof PropertyPermission) {
            return;
        }
        if (perm instanceof ReflectPermission) {
            if (perm.getName().equals("suppressAccessChecks")) {
                if (isSystemClass(getClassContext()[3])) {
                    return;
                }
            }
        }
        super.checkPermission(perm);
    }

    private static boolean isSystemClass(Class<?> clazz) {
        return clazz.getClassLoader() == null;
    }

    @Override
    public ThreadGroup getThreadGroup() {
        if (rootGroup == null) {
            rootGroup = getRootGroup();
        }
        return rootGroup;
    }

    private static ThreadGroup getRootGroup() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        while (root.getParent() != null) {
            root = root.getParent();
        }
        return root;
    }

    void unlock(long unlockCode) {
        if (this.unlockCode == unlockCode) {
            locked = false;
            return;
        }
        throw new SecurityException();
    }
}
