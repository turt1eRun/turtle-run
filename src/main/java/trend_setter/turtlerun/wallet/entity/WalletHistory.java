package trend_setter.turtlerun.wallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import trend_setter.turtlerun.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wallet_histories")
public class WalletHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_history_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private long transactionId;

    @Column(precision = 19, nullable = false)
    private BigDecimal previousBalance;

    @Column(precision = 19, nullable = false)
    private BigDecimal changeAmount;

    @Column(precision = 19, nullable = false)
    private BigDecimal currentBalance;

    @Builder
    public WalletHistory(Wallet wallet, TransactionType transactionType, long transactionId,
        BigDecimal previousBalance, BigDecimal changeAmount, BigDecimal currentBalance) {
        this.wallet = wallet;
        this.transactionType = transactionType;
        this.transactionId = transactionId;
        this.previousBalance = previousBalance;
        this.changeAmount = changeAmount;
        this.currentBalance = currentBalance;
    }
}
