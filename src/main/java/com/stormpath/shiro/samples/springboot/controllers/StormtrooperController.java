package com.stormpath.shiro.samples.springboot.controllers;

import com.stormpath.shiro.samples.springboot.common.dao.StormtrooperDao;
import com.stormpath.shiro.samples.springboot.common.model.Stormtrooper;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/troopers",
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StormtrooperController {

    private final StormtrooperDao trooperDao;

    @Autowired
    public StormtrooperController(StormtrooperDao trooperDao) {
        this.trooperDao = trooperDao;
    }

    @GetMapping()
    @RequiresPermissions("troopers:read")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "officer", "underling"})
    public Collection<Stormtrooper> listTroopers() {
        return trooperDao.listStormtroopers();
    }

    @GetMapping(path = "/{id}")
    @RequiresPermissions("troopers:read")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "officer", "underling"})
    public Stormtrooper getTrooper(@PathVariable("id") String id) throws NotFoundException {

        Stormtrooper stormtrooper = trooperDao.getStormtrooper(id);
        if (stormtrooper == null) {
            throw new NotFoundException(id);
        }
        return stormtrooper;
    }

    @PostMapping()
    @RequiresPermissions("troopers:create")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "officer"})
    public Stormtrooper createTrooper(@RequestBody Stormtrooper trooper) {

        return trooperDao.addStormtrooper(trooper);
    }

    @PostMapping(path = "/{id}")
    @RequiresPermissions("troopers:update")
    @RequiresRoles(logical = Logical.OR, value = {"admin", "officer"})
    public Stormtrooper updateTrooper(@PathVariable("id") String id, @RequestBody Stormtrooper updatedTrooper) throws NotFoundException {

        return trooperDao.updateStormtrooper(id, updatedTrooper);
    }


    @DeleteMapping(path = "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequiresPermissions("troopers:delete")
    @RequiresRoles("admin")
    public void deleteTrooper(@PathVariable("id") String id) {
        trooperDao.deleteStormtrooper(id);
    }


}
