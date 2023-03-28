package com.example.online_bookstore.service.implementation;

import com.example.online_bookstore.dto.request.OrderDTO;
import com.example.online_bookstore.dto.response.BookOrderResponse;
import com.example.online_bookstore.dto.response.OrderResponse;
import com.example.online_bookstore.entity.Book;
import com.example.online_bookstore.entity.BookOrder;
import com.example.online_bookstore.entity.Customer;
import com.example.online_bookstore.entity.Order;
import com.example.online_bookstore.enums.BookType;
import com.example.online_bookstore.exception.ResourceNotFoundException;
import com.example.online_bookstore.repository.BookOrderRepository;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CustomerRepository;
import com.example.online_bookstore.repository.OrderRepository;
import com.example.online_bookstore.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.example.online_bookstore.service.implementation.CustomerServiceImplementation.getCustomerResponse;

@Service
public class OrderServiceImplementation implements OrderService {

    private final CustomerRepository customerRepository;

    private final BookRepository bookRepository;

    private final OrderRepository orderRepository;

    private final BookOrderRepository bookOrderRepository;

    public OrderServiceImplementation(CustomerRepository customerRepository,
                                      BookRepository bookRepository,
                                      OrderRepository orderRepository,
                                      BookOrderRepository bookOrderRepository) {
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.bookOrderRepository = bookOrderRepository;
    }

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO, Long customerId) {
        var optionalCustomer = customerRepository.findById(customerId);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Not found customer with given id: %d".formatted(customerId));
        }
        return makeOrder(orderDTO, optionalCustomer.get());
    }

    private OrderResponse makeOrder(OrderDTO orderDTO, Customer customer) {
        var filteredBooks = orderDTO.booksIds().stream()
                .map(bookRepository::findById)
                .filter(Optional::isPresent)
                .toList();

        if (filteredBooks.isEmpty()) {
            throw new ResourceNotFoundException("There are no existing books in provided id list: %s".formatted(orderDTO.booksIds()));
        }

        var orderedBooks = filteredBooks.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        var order = orderRepository.save(getPreparedOrder(customer, orderedBooks));

        var bookOrders = order.getBookOrders().stream().peek(bookOrder -> bookOrder.setOrder(order)).toList();
        bookOrderRepository.saveAll(bookOrders);

        // right price
        order.setTotalPrice(calculateTotalPrice(order.getBookOrders()));
        orderRepository.save(order);

        return createOrderResponse(order);
    }

    private Order getPreparedOrder(Customer customer, List<Book> orderedBooks) {
        var ref = new Object() {
            BigInteger loyaltyPoints = customer.getLoyaltyPoints();
            final Set<BookOrder> bookOrderResponses = new HashSet<>();
        };

        orderedBooks.forEach(book -> {
            var bookOrder = createBookOrder(orderedBooks.size(), book);
            ref.loyaltyPoints = ref.loyaltyPoints.add(BigInteger.ONE);
            if ((ref.loyaltyPoints.equals(BigInteger.TEN) || ref.loyaltyPoints.compareTo(BigInteger.TEN) > 0) && !bookOrder.getBook().getType().equals(BookType.NEW)) {
                bookOrder.setSellingPrice(BigDecimal.ZERO);
                ref.loyaltyPoints = ref.loyaltyPoints.subtract(BigInteger.TEN);
            }
            ref.bookOrderResponses.add(bookOrder);
        });

        // check if previous
        while (ref.loyaltyPoints.equals(BigInteger.TEN) || ref.loyaltyPoints.compareTo(BigInteger.TEN) > 0) {
            if (ref.loyaltyPoints.equals(BigInteger.TEN) || ref.loyaltyPoints.compareTo(BigInteger.TEN) > 0) {
                var bookOrderForDiscount = ref.bookOrderResponses.stream().sorted(Comparator.comparing(BookOrder::getSellingPrice))
                        .filter(b -> !b.getBook().getType().equals(BookType.NEW) && !b.getSellingPrice().equals(BigDecimal.ZERO)).findFirst();
                if (bookOrderForDiscount.isEmpty()) {
                    break;
                } else {
                    bookOrderForDiscount.get().setSellingPrice(BigDecimal.ZERO);
                    ref.loyaltyPoints = ref.loyaltyPoints.subtract(BigInteger.TEN);
                }
            }
        }

        customer.setLoyaltyPoints(ref.loyaltyPoints);
        customerRepository.save(customer);

        return Order.builder()
                .customer(customer)
                .bookOrders(ref.bookOrderResponses)
                .totalPrice(BigDecimal.ZERO)
                .build();
    }

    private OrderResponse createOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                getBookOrderResponse(order.getBookOrders()),
                getCustomerResponse(order.getCustomer()));
    }

    private List<BookOrderResponse> getBookOrderResponse(Set<BookOrder> bookOrders) {
        return bookOrders.stream()
                .map(bookOrder -> new BookOrderResponse(
                        bookOrder.getBook().getTitle(),
                        bookOrder.getBook().getAuthor(),
                        bookOrder.getSellingPrice()))
                .toList();
    }

    private BigDecimal calculateTotalPrice(Set<BookOrder> bookOrderResponses) {
        return bookOrderResponses.stream()
                .map(BookOrder::getSellingPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BookOrder createBookOrder(int bundleSize, Book book) {
        return BookOrder.builder()
                .book(book)
                .sellingPrice(calculateSellingPrice(book, bundleSize))
                .build();
    }

    private BigDecimal calculateSellingPrice(Book book, int bundleSize) {
        switch (book.getType()) {
            case NEW -> {
                return book.getPrice();
            }
            case REGULAR -> {
                if (bundleSize > 2) {
                    return book.getPrice().multiply(new BigDecimal("0.9"));
                } else {
                    return book.getPrice();
                }
            }
            case OLD -> {
                if (bundleSize > 2) {
                    return book.getPrice().multiply(new BigDecimal("0.75"));
                } else {
                    return book.getPrice().multiply(new BigDecimal("0.8"));
                }
            }
            default -> throw new ResourceNotFoundException("Not existing type of book");
        }
    }
}
