import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

public class LeoSeleniumTest {
    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    public static String getAbsoluteXpath(WebElement element){
        int n = element.findElements(By.xpath("./ancestor::*")).size();
        String path = "";
        WebElement current = element;
        for(int i = n; i > 0; i--){
            String tag = current.getTagName();
            int lvl = current.findElements(By.xpath("./preceding-sibling::" + tag)).size() + 1;
            path = String.format("/%s[%d]%s", tag, lvl, path);
            current = current.findElement(By.xpath("./parent::*"));
        }
        return "/" + current.getTagName() + path;
    }
    public static String getRelativeXpath(WebElement self, WebElement ancestor){
        int a = ancestor.findElements(By.xpath("./ancestor::*")).size();
        int s = self.findElements(By.xpath("./ancestor::*")).size();
        String path = "";
        WebElement current = self;
        for(int i = s - a; i > 0; i--){
            String tag = current.getTagName();
            int lvl = current.findElements(By.xpath("./preceding-sibling::" + tag)).size() + 1;
            path = String.format("/%s[%d]%s", tag, lvl, path);
            current = current.findElement(By.xpath("./parent::*"));
        }
        return "/"+ path;
    }
    public void getAllAbsolutePaths(WebDriver driver,String xpath){
        List<WebElement> elements = driver.findElements(By.xpath(xpath));

        for(int i=1;i<elements.size();i++) {
            String absXpath = getAbsoluteXpath(elements.get(i));
            System.out.println(absXpath);
        }
    }

    public void getAllRelativePaths(WebDriver driver,String toElement, String fromElement){
        List<WebElement> elements = driver.findElements(By.xpath(toElement));
        WebElement from = driver.findElement(By.xpath(fromElement));
        for(int i=1;i<elements.size();i++) {
            String relXpath = getRelativeXpath(elements.get(i),from);
            System.out.println(relXpath);
        }
    }


    @Test
    public void Test()
    {
        WebDriver driver = new ChromeDriver();
        driver.get("http://dushyn.com/blog-Grid.html");
        getAllAbsolutePaths(driver,"//div/a[contains(@href,'/books/')]");
        getAllRelativePaths(driver,"//div/a[contains(@href,'/books/')]","//div[contains(@class,'container-fluid')]");
        driver.quit();
    }
}

