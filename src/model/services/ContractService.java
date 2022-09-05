package model.services;

import model.entities.Contract;
import model.entities.Installment;

import java.util.Calendar;
import java.util.Date;

public class ContractService {
    private OnlinePaymentService onlinePaymentService;

    public ContractService(OnlinePaymentService onlinePaymentService) {
        this.onlinePaymentService = onlinePaymentService;
    }
    public void processContract(Contract contract, int months) {
        double basicInstallment = contract.getTotalValue()/months;
        for (int i=1; i<=months; i++) {
           double updatedInstallment = basicInstallment + onlinePaymentService.interest(basicInstallment, i);
            double fullInstallment = updatedInstallment + onlinePaymentService.paymentFee(updatedInstallment);
            Date dueDate = addMonths(contract.getDate(),i);
            contract.getInstallments().add(new Installment(dueDate, fullInstallment));
        }
    }
    private Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }
}