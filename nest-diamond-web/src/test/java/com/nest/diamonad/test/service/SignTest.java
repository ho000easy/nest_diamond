package com.nest.diamonad.test.service;

import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

public class SignTest {
    public static void main(String[] args) {
        String siweMessage = "app.odos.xyz wants you to sign in with your Ethereum account:\n" +
                "0x32ddaA2950400313b5b00B1802fFf8f6386B905A\n" +
                "\n" +
                "Sign in with Ethereum on Odos. This is NOT a transaction and does NOT give Odos or anyone else permission to send transactions or interact with your assets. By signing in, you accept all terms at https://docs.odos.xyz/resources/policies/terms-of-use\n" +
                "\n" +
                "URI: https://app.odos.xyz/market\n" +
                "Version: 1\n" +
                "Chain ID: 8453\n" +
                "Nonce: 12c9e3134ed9479fa69b4e8e8035ce90\n" +
                "Issued At: 2025-12-10T08:09:09.642Z\n" +
                "Request ID: b809b180-fb07-4082-ae6b-d8b3aeb472d7";
        String messageHex = Numeric.toHexString(siweMessage.getBytes(StandardCharsets.UTF_8));
        System.out.println(messageHex);

        String hex = "0x6170702e6f646f732e78797a2077616e747320796f7520746f207369676e20696e207769746820796f757220457468657265756d206163636f756e743a0a3078333264646141323935303430303331336235623030423138303266466638663633383642393035410a0a5369676e20696e207769746820457468657265756d206f6e204f646f732e2054686973206973204e4f542061207472616e73616374696f6e20616e6420646f6573204e4f542067697665204f646f73206f7220616e796f6e6520656c7365207065726d697373696f6e20746f2073656e64207472616e73616374696f6e73206f7220696e746572616374207769746820796f7572206173736574732e204279207369676e696e6720696e2c20796f752061636365707420616c6c207465726d732061742068747470733a2f2f646f63732e6f646f732e78797a2f7265736f75726365732f706f6c69636965732f7465726d732d6f662d7573650a0a5552493a2068747470733a2f2f6170702e6f646f732e78797a2f6d61726b65740a56657273696f6e3a20310a436861696e2049443a20383435330a4e6f6e63653a2031326339653331333465643934373966613639623465386538303335636539300a4973737565642041743a20323032352d31322d31305430383a30393a30392e3634325a0a526571756573742049443a2062383039623138302d666230372d343038322d616536622d643862336165623437326437";
        String raw = new String(Numeric.hexStringToByteArray(hex), StandardCharsets.UTF_8);
        System.out.println(raw);
    }
}
