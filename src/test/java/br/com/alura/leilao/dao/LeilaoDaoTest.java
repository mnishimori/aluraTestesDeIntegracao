package br.com.alura.leilao.dao;

import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class LeilaoDaoTest {

    private LeilaoDao leilaoDao;

    private EntityManager em;

    @BeforeEach
    void beforeEach() {
        this.em = JPAUtil.getEntityManager();
        this.leilaoDao = new LeilaoDao(this.em);
        em.getTransaction().begin();
    }

    @AfterEach
    void afterEach() {
        this.em.getTransaction().rollback();
    }

    @Test
    void deveCadastrarUmLeilao() {
        // cenário
        Leilao leilao = this.getLeilao();

        // execução
        leilao = this.leilaoDao.salvar(leilao);

        // verificação
        Leilao leilaoSalvo = this.leilaoDao.buscarPorId(leilao.getId());
        Assertions.assertNotNull(leilaoSalvo);
    }

    @Test
    void deveBuscarUmLeilaoPorId() {
        // cenário
        Leilao leilao = this.getLeilao();
        leilao = this.leilaoDao.salvar(leilao);

        // execução e verificação
        Leilao leilaoSalvo = this.leilaoDao.buscarPorId(leilao.getId());
        Assertions.assertNotNull(leilaoSalvo);
    }

    @Test
    void deveFalharAoBuscarUmLeilaoPorIdInvalido() {
        // cenário
        Leilao leilao = this.getLeilao();
        leilao.setId(10L);

        // execução e verificação
        Leilao leilaoNaoEncontrado = this.leilaoDao.buscarPorId(leilao.getId());
        Assertions.assertNull(leilaoNaoEncontrado);
    }

    @Test
    void deveBuscarUmaListaDeLeiloes() {
        // cenário
        Leilao leilao = this.getLeilao();
        leilao = this.leilaoDao.salvar(leilao);

        Leilao leilao1 = this.getLeilao();
        leilao1 = this.leilaoDao.salvar(leilao1);

        List<Leilao> leiloes = new ArrayList<>();
        leiloes.add(leilao);
        leiloes.add(leilao1);

        // execução
        List<Leilao> leilaoList = this.leilaoDao.buscarTodos();

        // verificação
        Assertions.assertNotNull(leilaoList);
        Assertions.assertEquals(leilaoList.size(), leiloes.size());
    }

    @Test
    void deveBuscarUmaListaDeLeiloesPorPeriodo() {
        // cenário
        Leilao leilao = this.getLeilao();
        leilao.setDataAbertura(LocalDate.of(2021,11,15));
        leilao = this.leilaoDao.salvar(leilao);

        Leilao leilao1 = this.getLeilao();
        leilao1.setDataAbertura(LocalDate.of(2021,11,16));
        leilao1 = this.leilaoDao.salvar(leilao1);

        Leilao leilao2 = this.getLeilao();
        leilao2.setDataAbertura(LocalDate.of(2021,11,17));
        leilao2 = this.leilaoDao.salvar(leilao2);

        List<Leilao> leiloes = new ArrayList<>();
        leiloes.add(leilao);
        leiloes.add(leilao1);
        leiloes.add(leilao2);

        // execução
        List<Leilao> leilaoList = this.leilaoDao.buscarLeiloesDoPeriodo(
                LocalDate.of(2021, 11, 1),
                LocalDate.of(2021, 11, 29));

        // verificação
        Assertions.assertNotNull(leilaoList);
        Assertions.assertEquals(leiloes.size(), leilaoList.size());
    }

    @Test
    void deveBuscarListaLeiloesPorUsuario(){
        // cenário
        Leilao leilao = this.getLeilao();
        leilao = this.leilaoDao.salvar(leilao);

        // execução
        List<Leilao> leilaos = this.leilaoDao.buscarLeiloesDoUsuario(leilao.getUsuario());

        // verificação
        Assertions.assertNotNull(leilaos);
        Assertions.assertEquals(1, leilaos.size());
    }

    @Test
    void deveAtualizarUmLeilao() {
        // cenário
        Leilao leilao = getLeilao();
        leilao = this.leilaoDao.salvar(leilao);
        leilao.setNome("Caderno");
        leilao.setValorInicial(new BigDecimal("10"));

        // execução
        leilao = this.leilaoDao.salvar(leilao);

        // verificação
        Leilao leilaoSalvo = this.leilaoDao.buscarPorId(leilao.getId());
        Assertions.assertEquals(leilaoSalvo.getNome(), leilao.getNome());
        Assertions.assertEquals(leilaoSalvo.getValorInicial(), leilao.getValorInicial());
    }

    private Leilao getLeilao() {
        Usuario usuario = this.getUsuario();
        Leilao leilao = new Leilao("Mochila", new BigDecimal("70"), LocalDate.now(), usuario);
        return leilao;
    }

    private Usuario getUsuario() {
        Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
        em.persist(usuario);
        return usuario;
    }
}