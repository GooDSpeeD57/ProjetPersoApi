package com.monprojet.boutiquejeux.service;
import com.monprojet.boutiquejeux.dto.response.StockageResponse;
import com.monprojet.boutiquejeux.entity.Stockage;
import com.monprojet.boutiquejeux.exception.BusinessException;
import com.monprojet.boutiquejeux.exception.ResourceNotFoundException;
import com.monprojet.boutiquejeux.repository.StockageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class StockageService {
    private final StockageRepository stockageRepository;
    public List<StockageResponse> getStockMagasin(Long magasinId) { return stockageRepository.findAllByMagasinId(magasinId).stream().map(this::toResponse).toList(); }
    public List<StockageResponse> getStockProduit(Long produitId)  { return stockageRepository.findAllByProduitId(produitId).stream().map(this::toResponse).toList(); }
    @Transactional
    public StockageResponse updateStock(Long magasinId, Long produitId, Integer quantite) {
        if (quantite < 0) throw new BusinessException("Quantité ne peut être négative");
        Stockage s = stockageRepository.findByProduitIdAndMagasinId(produitId, magasinId).orElseThrow(() -> new ResourceNotFoundException("Stock introuvable"));
        s.setQuantite(quantite);
        return toResponse(stockageRepository.save(s));
    }
    private StockageResponse toResponse(Stockage s) { return new StockageResponse(s.getId(), s.getProduit().getNom(), s.getMagasin().getNom(), s.getQuantite()); }
}
