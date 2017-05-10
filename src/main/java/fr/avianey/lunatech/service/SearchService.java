package fr.avianey.lunatech.service;

import fr.avianey.lunatech.entity.Runway;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Stateless
@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class SearchService {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @GET
    public Response get(@QueryParam("q") String q) {
        Set<String> params = new HashSet<>();
        StringBuilder sb = new StringBuilder("SELECT r " +
                "FROM " + Runway.class.getSimpleName() + " r " +
                "JOIN FETCH r.airport a " +
                "JOIN FETCH a.country c");
        if (q != null && !q.isEmpty()) {
            sb.append(" WHERE c.name LIKE :qLike");
            params.add("qLike");
            if (q.length() == 2) {
                sb.append(" OR c.code = :qExact");
                params.add("qExact");
            }
        }
        TypedQuery<Runway> query = em.createQuery(sb.toString(), Runway.class);
        if (params.contains("qLike")) {
            query.setParameter("qLike", q + "%");
        }
        if (params.contains("qExact")) {
            query.setParameter("qExact", q);
        }
        return Response.ok(query.getResultList()).build();
    }

}
