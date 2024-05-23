package notebook.app;

import notebook.controller.UserController;
import notebook.model.repository.DBRepository;
import notebook.model.repository.GBRepository;
import notebook.model.repository.impl.FileDBRepository;
import notebook.model.repository.impl.UserRepository;
import notebook.view.UserView;

import static notebook.util.DBConnector.createDB;

public class Application {
    public void start(){
        try{
            run();
        } catch (Exception e){
            e.printStackTrace();
            restart();
        }
    }

    private void restart(){
        System.out.println("restarted");
        run();
    }

    private void run(){
        createDB();
        DBRepository fileDBRepository = new FileDBRepository();
        GBRepository repository = new UserRepository(fileDBRepository);
        UserController controller = new UserController(repository);
        UserView view = new UserView(controller);
        view.run();
    }
}
