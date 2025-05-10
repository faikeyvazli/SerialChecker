import java.util.*;

class Operation{
    private String transactionId;
    private int time;
    private String operation;
    private String dataItem;

    public Operation(String transactionId, int time, String operation, String dataItem) {
        this.transactionId = transactionId;
        this.time = time;
        this.operation = operation;
        this.dataItem = dataItem;
    }

    public String getDataItem() {
        return dataItem;
    }

    public void setDataItem(String dataItem) {
        this.dataItem = dataItem;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "transactionId='" + transactionId + '\'' +
                ", time=" + time +
                ", operation='" + operation + '\'' +
                ", dataItem='" + dataItem + '\'' +
                '}';
    }
}

public class Serial {
    static Map<String, String> graphMapping = new HashMap<>();

    static List<Operation> operations = new ArrayList<Operation>();

    static void readFileInput() {
        Scanner sc;

        sc = new Scanner(System.in);
        int tCnt = 0;
        String tCount = null;
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("T")) {
                tCnt++;
                tCount = 'T' + Integer.toString(tCnt);
            } else {
                int time = Integer.parseInt(input.substring(0, input.indexOf(',')));
                String operation = input.substring(input.indexOf(',') + 1, input.indexOf('('));
                String dataItem = input.substring(input.indexOf('(') + 1, input.indexOf(')'));
                Operation op = new Operation(tCount, time, operation, dataItem);
                operations.add(op);
            }
        }
        for (Operation op : operations) {
            System.out.println(op);
        }
    }

    static void buildPrecGraph(Operation op1, Operation op2) {
        graphMapping.put(op1.getTransactionId(), op2.getTransactionId());
    }

    static void detectConflicts(List<Operation> operations) {
        for (int i = 0; i < operations.size(); i++) {
            Operation op1 = operations.get(i);
            // avoid the comparison with itself
            for (int j = 0; j < operations.size(); j++) {
                if (i == j) {
                    continue;
                }
                Operation op2 = operations.get(j);
                // then, check that we are looking at operations of different transactions
                if (!op1.getTransactionId().equals(op2.getTransactionId()) && op1.getDataItem().equals(op2.getDataItem())) {
                    if (op1.getOperation().equals("READ") && op2.getOperation().equals("READ")) {
                        continue;
                    } else {
                        if (op1.getTime() < op2.getTime()) {
                            buildPrecGraph(op1, op2);
                        } else {
                            buildPrecGraph(op2, op1);
                        }
                    }
                }


            }
        }

    }

    public static void main(String[] args) {
        readFileInput();
        detectConflicts(operations);
        System.out.println(Collections.singletonList(graphMapping));
    }
}