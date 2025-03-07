package es.santander.ascender.ejerc008.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.santander.ascender.ejerc008.Repository.PersonaRepository;
import es.santander.ascender.ejerc008.Repository.ProvinciaRepository;
import es.santander.ascender.ejerc008.model.Persona;
import es.santander.ascender.ejerc008.model.Provincia;

@Service
@Transactional
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public Persona createPersona(Persona persona) {
        estableceProvincia(persona, persona); // Establece una nueva provincia en caso de cambiar la misma.
        return personaRepository.save(persona);
    }

    @Transactional(readOnly = true)
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Persona> getPersonaById(Long id) {
        return personaRepository.findById(id);
    }

    public List<Persona> getPersonasByProvincia(Long provinciaId) {
        return personaRepository.findByProvinciaId(provinciaId);
    }

    public Persona updatePersona(Long id, Persona personaDetails) {
        Optional<Persona> personaOptional = personaRepository.findById(id);
        if (personaOptional.isPresent()) {
            Persona persona = personaOptional.get();
            persona.setNombre(personaDetails.getNombre());
            persona.setApellidos(personaDetails.getApellidos());


            estableceProvincia(personaDetails, persona);
            
            return personaRepository.save(persona);
        
        }
        return null;
    }

    private void estableceProvincia(Persona personaDetails, Persona persona) {
        Provincia provincia = null;
        if( personaDetails.getProvincia() != null && personaDetails.getProvincia().getId() != null){
          provincia = provinciaRepository.findById(personaDetails.getProvincia().getId()).orElse(null);
        }
        
        persona.setProvincia(provincia);
    }

    public boolean deletePersona(Long id) {
        if (personaRepository.existsById(id)) {
            personaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
