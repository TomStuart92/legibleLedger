package xyzCoin;

import org.junit.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by Tom on 14/12/2017.
 *
 * Tests Wallet Key Generator
 */
public class WalletKeyGeneratorTest {
  @Test
  public void createKeysShouldReturnKeyPair() throws Exception {
    WalletKeyGenerator tester = new WalletKeyGenerator(1048, "RSA");
    assertThat(tester.createKeys(), instanceOf(KeyPair.class));
  }

  @Test
  public void getPrivateKeyShouldReturnPrivateKey() throws Exception {
    WalletKeyGenerator tester = new WalletKeyGenerator(1048, "RSA");
    tester.createKeys();
    assertThat(tester.getPrivateKey(), instanceOf(PrivateKey.class));
  }

  @Test
  public void getPublicKeyShouldReturnPublicKey() throws Exception {
    WalletKeyGenerator tester = new WalletKeyGenerator(1048, "RSA");
    tester.createKeys();
    assertThat(tester.getPublicKey(), instanceOf(PublicKey.class));
  }

}