abstract class Employee{
	public abstract double earning();
}

class YearWorker extends Employee{
	public double earning(){
		return 100000;
	}
}

class MonthWorker extends Employee{
	public double earning(){
		return 100;
	}
}

class WeekWorker extends Employee{
	public double earning(){
		return 1;
	}
}

class Company{
	Employee[] employee;
	double salaries=0;
    Company(Employee[] employee){
		this.employee=employee;
	}
	public double salariesPay(){
		salaries=0;
		for(int i=0;i<employee.length;i++)
			salaries += employee[i].earning();
        return salaries;
	}
}

public class HardWork{
	public static void main(String args[]){
		Employee[] employee=new Employee[20];
		for(int i=0;i<employee.length;i++){
			if(i%3==0)
				employee[i]=new WeekWorker();
			else if(i%3==1)
				employee[i]=new MonthWorker();
			else if(i%3==2)
				employee[i]=new YearWorker();
		}
		Company company=new Company(employee);
		System.out.println("Total:"+company.salariesPay());
	}
}