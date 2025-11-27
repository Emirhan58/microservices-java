package com.mrkiwix.accounts.service.impl;

import com.mrkiwix.accounts.dto.AccountsDto;
import com.mrkiwix.accounts.dto.CardsDto;
import com.mrkiwix.accounts.dto.CustomerDetailsDto;
import com.mrkiwix.accounts.dto.LoansDto;
import com.mrkiwix.accounts.entities.Accounts;
import com.mrkiwix.accounts.entities.Customer;
import com.mrkiwix.accounts.exception.ResourceNotFoundException;
import com.mrkiwix.accounts.mapper.AccountsMapper;
import com.mrkiwix.accounts.mapper.CustomerMapper;
import com.mrkiwix.accounts.repository.AccountsRepository;
import com.mrkiwix.accounts.repository.CustomerRepository;
import com.mrkiwix.accounts.service.ICustomersService;
import com.mrkiwix.accounts.service.client.CardsFeignClient;
import com.mrkiwix.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber,  String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDto = loansFeignClient.fetchLoanDetails(correlationId ,mobileNumber);
        if(loansDto != null) {
            customerDetailsDto.setLoansDto(loansDto.getBody());
        }

        ResponseEntity<CardsDto> cardsDto = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if(cardsDto != null) {
            customerDetailsDto.setCardsDto(cardsDto.getBody());
        }

        return customerDetailsDto;
    }
}
