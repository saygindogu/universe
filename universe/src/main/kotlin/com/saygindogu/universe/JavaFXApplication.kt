import com.saygindogu.universe.StageReady
import com.saygindogu.universe.UniverseApplication
import javafx.application.Application
import javafx.application.HostServices
import javafx.application.Platform
import javafx.stage.Stage
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.support.GenericApplicationContext
import java.util.function.Supplier

class JavaFxApplication : Application() {
    lateinit var context: ConfigurableApplicationContext

    override fun init() {
        class Initilizer : ApplicationContextInitializer<GenericApplicationContext> {
            override fun initialize(applicationContext: GenericApplicationContext) {

                applicationContext.registerBean(Application::class.java, Supplier<Application> {
                    this@JavaFxApplication
                })
                applicationContext.registerBean(Parameters::class.java, Supplier<Parameters> {
                    parameters
                })
                applicationContext.registerBean(HostServices::class.java, Supplier<HostServices> {
                    hostServices
                })
            }
        }
        context = SpringApplicationBuilder()
            .sources(UniverseApplication::class.java)
            .initializers(Initilizer())
            .run(*parameters.raw.toTypedArray())
    }

    override fun stop() {
        context.stop()
        Platform.exit()
    }

    override fun start(primaryStage: Stage?) {
        context.publishEvent(StageReady(primaryStage!!))
    }
}