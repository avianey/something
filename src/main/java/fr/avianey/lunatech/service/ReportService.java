package fr.avianey.lunatech.service;

import fr.avianey.lunatech.entity.Airport;
import fr.avianey.lunatech.entity.Runway;
import fr.avianey.lunatech.entity.wrapper.CountryWrapper;
import fr.avianey.lunatech.entity.wrapper.IdentWrapper;
import fr.avianey.lunatech.entity.wrapper.SurfaceWrapper;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Stateless
@Path("/reporting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class ReportService {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    @GET
    @Path("/countries/top")
    public Response topCountries(@DefaultValue("10") @QueryParam("limit") int limit,
                                 @DefaultValue("desc") @QueryParam("order") String order) {
        TypedQuery<CountryWrapper> query = em.createQuery(
                "SELECT NEW " + CountryWrapper.class.getName() + "(c, COUNT(a)) " +
                        "FROM " + Airport.class.getSimpleName() + " a " +
                        "JOIN FETCH a.country c " +
                        "GROUP BY c.code " +
                        "ORDER BY COUNT(a) " + ("desc".equalsIgnoreCase(order) ? "DESC" : "ASC"), CountryWrapper.class);
        query.setMaxResults(limit);
        return Response.ok(query.getResultList()).build();
    }

    @GET
    @Path("/countries/surfaces")
    public Response countrySurfaces() {
        TypedQuery<SurfaceWrapper> query = em.createQuery(
                "SELECT NEW " + SurfaceWrapper.class.getName() + "(c, r.surface) " +
                        "FROM " + Runway.class.getSimpleName() + " r " +
                        "JOIN FETCH r.airport a " +
                        "JOIN FETCH a.country c " +
                        "GROUP BY r.surface, c ", SurfaceWrapper.class);
        return Response.ok(query.getResultList()).build();
    }

    @GET
    @Path("/surfaces/idents")
    public Response surfaceIdents() {
        TypedQuery<IdentWrapper> query = em.createQuery(
                "SELECT NEW " + IdentWrapper.class.getName() + "(r.ident, COUNT(1)) " +
                        "FROM " + Runway.class.getSimpleName() + " r " +
                        "GROUP BY r.ident " +
                        "ORDER BY COUNT(1) DESC", IdentWrapper.class);
        query.setMaxResults(10);
        return Response.ok(query.getResultList()).build();
    }

}
