package com.poultryfarm.server;

import java.util.Properties;

@FunctionalInterface
public interface GetHandler {
    String handle(Properties parameters);
}
