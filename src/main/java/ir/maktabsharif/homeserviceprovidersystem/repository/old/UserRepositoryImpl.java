//package ir.maktabsharif.homeserviceprovidersystem.repository;
//
//import ir.maktabsharif.homeserviceprovidersystem.entity.User;
//import ir.maktabsharif.homeserviceprovidersystem.exception.ResourceNotFoundException;
//
//import java.util.Optional;
//
//
//public class UserRepositoryImpl<T extends User> extends CrudRepositoryImpl<T, Long> implements UserRepository<T> {
//
//
//    protected UserRepositoryImpl(Class<T> entityClass) {
//        super(entityClass);
//    }
//
//    @Override
//    public Optional<T> findByEmail(String email) {
//        return entityManager.createQuery("select u from " + entityClass.getSimpleName() + " u" + " where u.email = :email", entityClass)
//                .setParameter("email", email)
//                .getResultList().stream().findFirst();
//    }
//}
