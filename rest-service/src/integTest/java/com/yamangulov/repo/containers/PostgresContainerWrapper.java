package com.yamangulov.repo.containers;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainerWrapper extends PostgreSQLContainer<PostgresContainerWrapper> {
    private static final String POSTGRES_IMAGE_NAME = "postgres:14.4";
    private static final String POSTGRES_DB = "users";
    private static final String POSTGRES_USER = "postgres";
    private static final String POSTGRES_PASSWORD = "postgres";

    public PostgresContainerWrapper() {
        super(POSTGRES_IMAGE_NAME);
        this
                .withDatabaseName(POSTGRES_DB)
                .withUsername(POSTGRES_USER)
                .withPassword(POSTGRES_PASSWORD);
    }

    @Override
    public void start() {
        super.start();
        this.getContainerId();
        // debug point. Container has to be already started
    }
}
