/**
 * Copyright Alex Objelean
 */
package ro.isdc.wro.maven.plugin;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * Test class for {@link CssHintMojo}
 * 
 * @author Alex Objelean
 */
public class TestCssLintMojo {
  private CssLintMojo mojo;
  
  @Before
  public void setUp()
      throws Exception {
    mojo = new CssLintMojo();
    mojo.setIgnoreMissingResources(false);
    setWroWithValidResources();
    mojo.setTargetGroups("g1");
    mojo.setMavenProject(Mockito.mock(MavenProject.class));
  }
  
  private void setWroFile(final String classpathResourceName)
      throws URISyntaxException {
    final URL url = getClass().getClassLoader().getResource(classpathResourceName);
    final File wroFile = new File(url.toURI());
    mojo.setWroFile(wroFile);
    mojo.setContextFolder(wroFile.getParentFile().getParentFile());
  }
  
  private void setWroWithValidResources()
      throws Exception {
    setWroFile("wro.xml");
  }
  
  private void setWroWithInvalidResources()
      throws Exception {
    setWroFile("wroWithInvalidResources.xml");
  }
  
  @Test
  public void testMojoWithPropertiesSet()
      throws Exception {
    mojo.setTargetGroups("valid");
    mojo.setIgnoreMissingResources(true);
    mojo.execute();
  }
  
  @Test(expected = MojoExecutionException.class)
  public void cannotExecuteWhenInvalidResourcesPresentAndDoNotIgnoreMissingResources()
      throws Exception {
    setWroWithInvalidResources();
    mojo.setIgnoreMissingResources(false);
    mojo.execute();
  }
  
  @Test
  public void testWroXmlWithInvalidResourcesAndIgnoreMissingResourcesTrue()
      throws Exception {
    setWroWithInvalidResources();
    mojo.setIgnoreMissingResources(true);
    mojo.execute();
  }
  
  @Test(expected = MojoExecutionException.class)
  public void testResourceWithErrors()
      throws Exception {
    mojo.setTargetGroups("invalid");
    mojo.execute();
  }
  
  @Test
  public void testErrorsWithNoFailFast()
      throws Exception {
    mojo.setFailNever(true);
    mojo.setOptions("undef, browser");
    mojo.setTargetGroups("undef");
    mojo.execute();
  }
  
  @Test
  public void shouldAnalyzeValidResources()
      throws Exception {
    mojo.setTargetGroups("valid");
    mojo.execute();
  }
  
  @Test(expected = MojoExecutionException.class)
  public void shouldAnalyzeInvalidResources()
      throws Exception {
    mojo.setTargetGroups("invalidResources");
    mojo.execute();
  }
  
  @Test
  public void shouldNotFailWhenAnalyzeInvalidResources()
      throws Exception {
    mojo.setFailNever(true);
    mojo.setTargetGroups("invalidResources");
    mojo.execute();
  }
  
  @Test
  public void testEmptyOptions()
      throws Exception {
    mojo.setOptions("");
    mojo.setTargetGroups("undef");
    mojo.execute();
  }
}
