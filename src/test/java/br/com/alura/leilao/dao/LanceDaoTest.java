package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;

class LanceDaoTest {

    private LanceDao lanceDao;

    private EntityManager em;


    @BeforeEach
    void beforEach(){
        this.em = JPAUtil.getEntityManager();
        this.lanceDao = new LanceDao(this.em);
        em.getTransaction().begin();
    }

    @AfterEach
    void afterEach() {
        this.em.getTransaction().rollback();
    }

    @Test
    void deveCadastrarUmLance() {
        // cenário
        Leilao leilao = this.getLeilao();
        Lance lance = getLance(leilao, new BigDecimal("100"));

        // execução
        this.lanceDao.salvar(lance);

        // verificação
        Lance lanceSalvo = this.lanceDao.buscarMaiorLanceDoLeilao(lance.getLeilao());
        Assertions.assertNotNull(lanceSalvo);
        Assertions.assertEquals(new BigDecimal("100"), lance.getValor());
    }

    @Test
    void deveBuscarMaiorLanceLeilao() {
        // cenário
        Leilao leilao = this.getLeilao();

        Lance lance = this.getLance(leilao, new BigDecimal("100"));
        this.lanceDao.salvar(lance);

        Lance lance2 = this.getLance(leilao, new BigDecimal("200"));
        this.lanceDao.salvar(lance2);

        // execução
        Lance lancePesquisa = this.lanceDao.buscarMaiorLanceDoLeilao(leilao);

        // verificação
        Assertions.assertNotNull(lancePesquisa);
        Assertions.assertEquals(new BigDecimal("200"), lancePesquisa.getValor());
    }

    private Lance getLance(Leilao leilao, BigDecimal valorLance) {
        Lance lance = new Lance(this.getUsuario(), valorLance);
        lance.setLeilao(leilao);
        return lance;
    }

    private Leilao getLeilao() {
        Leilao leilao = new Leilao("Mochila", new BigDecimal("70"), LocalDate.now(), this.getUsuario());
        this.em.persist(leilao);
        return leilao;
    }

    private Usuario getUsuario() {
        Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
        em.persist(usuario);
        return usuario;
    }

}