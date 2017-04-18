import criteriaordercaseinsensitive.A
import criteriaordercaseinsensitive.B
import grails.test.mixin.integration.Integration
import grails.transaction.Rollback
import org.grails.datastore.mapping.query.Query
import spock.lang.Specification

@Integration
@Rollback
class CriteriaTest extends Specification {

    void "Checks criteria ordering by B's property and ordering"(){
        given: 'Create some data'
        B b = new B(property1: 'A uppercase').save()
        A a1 = new A(b: b).save()
        b = new B(property1: 'a lowercase')
        A a2 = new A(b: b).save()
        b = new B(property1: 'B uppercase').save()
        A a3 = new A(b: b).save()
        b = new B(property1: 'b lowercase')
        A a4 = new A(b: b).save()

        expect: 'Criteria is done and the result is a1, a3, a2, a4'
        A.createCriteria().list() {
            createAlias('b', 'bAlias')
            order('bAlias.property1', 'asc')
        } == [a1, a3, a2, a4]
    }

    void "Checks criteria ordering by B's property and ordering, using Query.Order"(){
        given: 'Create some data'
        B b = new B(property1: 'A uppercase').save()
        A a1 = new A(b: b).save()
        b = new B(property1: 'a lowercase')
        A a2 = new A(b: b).save()
        b = new B(property1: 'B uppercase').save()
        A a3 = new A(b: b).save()
        b = new B(property1: 'b lowercase')
        A a4 = new A(b: b).save()

        expect: 'Criteria is not done, an exception is thrown and the result expected should be a1, a2, a3, a4'
        A.createCriteria().list() {
            createAlias('b', 'bAlias')
            order(new Query.Order('bAlias.property1', Query.Order.Direction.ASC).ignoreCase())
        } == [a1, a2, a3, a4]
    }
}
