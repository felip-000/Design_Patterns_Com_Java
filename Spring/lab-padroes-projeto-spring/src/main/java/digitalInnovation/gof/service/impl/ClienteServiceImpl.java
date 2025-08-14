package digitalInnovation.gof.service.impl;

import digitalInnovation.gof.model.Cliente;
import digitalInnovation.gof.model.ClienteRepository;
import digitalInnovation.gof.model.Endereco;
import digitalInnovation.gof.model.EnderecoRepository;
import digitalInnovation.gof.service.ClienteService;
import digitalInnovation.gof.service.ViaCepService;

import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id){
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente){
        salvarClienteComCep(cliente);
    }

    private void salvarClienteComCep(Cliente cliente) {
        //Verifica se o Endereco do Cliente já existe por CEP
        Endereco endereco = enderecoRepository.findById(cliente.getEndereco().getCep()).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cliente.getEndereco().getCep());
            enderecoRepository.save(novoEndereco);
            return null;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente){
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if(clienteBd.isPresent()){
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id){
        clienteRepository.deleteById(id);
    }
}
