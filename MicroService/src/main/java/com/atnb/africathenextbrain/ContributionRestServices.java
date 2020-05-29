package com.atnb.africathenextbrain;

import com.atnb.africathenextbrain.dao.ContributionRepository;
import com.atnb.africathenextbrain.entities.Contribution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContributionRestServices {
    @Autowired
    private ContributionRepository contributionRepository;

    //liste contribution
    @GetMapping(value = "/listContributions")
    public List<Contribution> listContribution(){
        return contributionRepository.findAll();
    }

    //details contribution
    @GetMapping(value = "/listContributions/{id}")
    public Contribution listContribution(@PathVariable(name="id") Long id){
        return contributionRepository.findById(id).get();
    }

    //update
    @PutMapping(value = "/listContributions/{id}")
    public Contribution update(@PathVariable(name="id") Long id,@RequestBody Contribution c){
        c.setId(id);
        return contributionRepository.save(c);
    }

    //enregistrer une contribution
    @PostMapping(value = "/listContributions")
    public Contribution save(@RequestBody Contribution c){
        return contributionRepository.save(c);
    }

    @DeleteMapping (value = "/listContributions/{id}")
    public void delete(@PathVariable(name="id") Long id){
        contributionRepository.deleteById(id);
    }



}
