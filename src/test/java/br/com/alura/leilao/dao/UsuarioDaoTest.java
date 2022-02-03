package br.com.alura.leilao.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.util.JPAUtil;

class UsuarioDaoTest {
	
	private UsuarioDao usuarioDao;
	
	private EntityManager em;
	
	@BeforeEach
	void beforeEach() {
		this.em = JPAUtil.getEntityManager();
		this.usuarioDao = new UsuarioDao(em);
		em.getTransaction().begin();
	}
	
	@AfterEach
	void afterEach() {
		em.getTransaction().rollback();
	}

	@Test
	void deveBuscarUsuarioPeloUserName() {
		// cenário
		Usuario usuario = getUsuario();

		// execução
		Usuario usuarioEncontrado = this.usuarioDao.buscarPorUsername(usuario.getNome());
		
		// verificação
		Assertions.assertNotNull(usuarioEncontrado);
		Assertions.assertEquals(usuarioEncontrado.getNome(), usuario.getNome());
	}

	@Test
	void naoDeveBuscarUsuarioNaoCadastradoPeloUserName() {
		// execução e verificação
		Assertions.assertThrows(NoResultException.class, () -> this.usuarioDao.buscarPorUsername("beltrano"));
	}

	@Test
	void deveDeletarUmUsuario() {
		// cenário
		Usuario usuario = this.getUsuario();

		// execução
		this.usuarioDao.deletar(usuario);

		// execução e verificação
		Assertions.assertThrows(NoResultException.class, () -> this.usuarioDao.buscarPorUsername(usuario.getNome()));
	}


	private Usuario getUsuario() {
		Usuario usuario = new Usuario("fulano", "fulano@email.com", "12345678");
		em.persist(usuario);
		return usuario;
	}

}
