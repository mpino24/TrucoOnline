package es.us.dp1.lx_xy_24_25.truco_beasts.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartaService {
    CartaRepository cartaRepository;


	@Autowired
	public CartaService(CartaRepository cartaRepository) {
		this.cartaRepository = cartaRepository;

  }


  @Transactional(readOnly = true)
	public Carta findCartaById(int cartaId) throws DataAccessException{
		return cartaRepository.findById(cartaId).get();
	
}
}