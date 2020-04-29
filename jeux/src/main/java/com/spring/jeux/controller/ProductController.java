package com.spring.jeux.controller;


import com.spring.jeux.exceptions.ProduitIntrouvableException;
import com.spring.jeux.model.Jeu;
import com.spring.jeux.dao.ProductDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;


@Api(description = "Gestion des jeux qu'on a")
@RestController
public class ProductController {


    @Autowired
    private ProductDao dao;

    @ApiOperation(value = "Affiche tous les jeux qu'on possède")
    @GetMapping(value="produits")
    public List<Jeu> listeProduits() {
        return dao.findAll();
    }

    @ApiOperation(value = "Récupère un jeu à travers son ID")
    @GetMapping(value="produits/{id}")
    public Jeu afficherProduit(@PathVariable int id) throws ProduitIntrouvableException{
        Jeu game = dao.findById(id);
        if(game==null) {
            throw new ProduitIntrouvableException("Le produit avec l'id "+ id + " est introuvable");
        }

        return game;
    }

    /*@ApiOperation(value = "Affiche tous les jeux dont le prix est plus cher que le prix donné dans le path")
    @GetMapping(value="produits/gt/{prix}")
    public List<Jeu> plusCherQue(@PathVariable int prix) {return dao.findByPrixGreaterThan(prix);}

    @ApiOperation(value = "Affiche tous les jeux dont le prix est moins cher que le prix donné dans le path")
    @GetMapping(value="produits/lt/{prix}")
    public List<Jeu> moinsCherQue(@PathVariable int prix) {return dao.findByPrixLessThan(prix);}*/

    /*@GetMapping(value="produits/max")
    public Jeu plusChere() {return dao.findMaxPrice();}*/


    @ApiOperation(value = "Ajoute le jeu reçu dans le body de la requête")
    @PostMapping(value="produits")
    public ResponseEntity<Void> ajouterProduit(@RequestBody Jeu game) {

        Jeu result = dao.save(game);
        System.out.println(game);
        if(result == null) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId())
                .toUri();

         return ResponseEntity.created(location).build();
    }
}