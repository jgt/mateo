/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaMayorDao;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author nujev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class CuentaMayorControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(CuentaMayorControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private CuentaMayorDao cuentaMayorDao;

    public CuentaMayorControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void debieraMostrarListaDeCuentaMayor() throws Exception {
        log.debug("Debiera monstrar lista de cuentas de mayor");

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_MAYOR))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_MAYOR_LISTA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_MAYORES))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS))
                .andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void debieraMostrarCuentaMayor() throws Exception {
        log.debug("Debiera mostrar cuenta de mayor");
        CuentaMayor cuentaMayor = new CuentaMayor("test1", "test");
        cuentaMayor = cuentaMayorDao.crea(cuentaMayor);

        this.mockMvc.perform(get(Constantes.PATH_CUENTA_MAYOR_VER +"/"+ cuentaMayor.getId()))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_CUENTA_MAYOR_VER + ".jsp"))
                .andExpect(model()
                .attributeExists(Constantes.ADDATTRIBUTE_MAYOR));
    }

    @Test
    public void debieraCrearCuentaMayor() throws Exception {
        log.debug("Debiera crear cuenta de mayor");

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_MAYOR_CREA)
                .param("nombre", "test2")
                .param("nombreFiscal", "test"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaMayor.creada.message"));
    }

    @Test
    public void debieraActualizarCuentaMayor() throws Exception {
        log.debug("Debiera actualizar cuenta de mayor");

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_MAYOR_ACTUALIZA)
                .param("nombre", "test3")
                .param("nombreFiscal", "test"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaMayor.actualizada.message"));
    }

    @Test
    public void debieraEliminarCtaMayor() throws Exception {
        log.debug("Debiera eliminar cuenta de mayor");
        CuentaMayor cuentaMayor = new CuentaMayor("test", "test");
        cuentaMayorDao.crea(cuentaMayor);

        this.mockMvc.perform(post(Constantes.PATH_CUENTA_MAYOR_ELIMINA)
                .param("id", cuentaMayor.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaMayor.eliminada.message"));
    }
}
