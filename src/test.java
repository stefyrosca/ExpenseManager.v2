import ro.ubbcluj.cs.Repository.UserXmlRepository;

/**
 * Created by ZUZU on 15.11.2015.
 */
public class test {
    public static void main(String[] args) {
        UserXmlRepository repository = null;
        try {
            repository = new UserXmlRepository("src\\resources\\users.xml");
            System.out.println(repository.findAll());
            repository.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
