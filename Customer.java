import java.util.function.Supplier;

public class Customer implements Comparable<Customer> {
    private final int id;
    private final Supplier<Double> serviceSupplier;

    public Customer(int id, Supplier<Double> serviceSupplier) {
        this.id = id;
        this.serviceSupplier = serviceSupplier;
    }

    public double generateServiceTime() {
        return serviceSupplier.get();
    }

    @Override
    public int compareTo(Customer o) {
        return o.id - this.id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}
