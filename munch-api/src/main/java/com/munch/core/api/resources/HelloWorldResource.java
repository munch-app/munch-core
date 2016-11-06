package com.munch.core.api.resources;

import com.codahale.metrics.annotation.Timed;
import com.munch.core.struct.rdbms.admin.AdminAccount;
import com.scottescue.dropwizard.entitymanager.UnitOfWork;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 6/11/2016
 * Time: 10:02 PM
 * Project: munch-core
 */
@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final EntityManager entityManager;

    @Inject
    public HelloWorldResource(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @GET
    @Timed
    @UnitOfWork
    @Path("/{id}")
    public HelloWorld sayHello(@PathParam("id") long id) {
        List<AdminAccount> list = entityManager
                .createQuery("SELECT a FROM AdminAccount a", AdminAccount.class).getResultList();
        return new HelloWorld(id, list.get(0).getName());
    }
}
