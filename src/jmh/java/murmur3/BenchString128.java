package murmur3;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.apache.commons.text.StringEscapeUtils;
import org.openjdk.jmh.annotations.*;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author <a href="mailto:konstantin.sobolev@gmail.com">Konstantin Sobolev</a>
 */
@SuppressWarnings("UnstableApiUsage")
@Warmup(iterations = 10)
@Measurement(iterations = 20)
public class BenchString128 {

  @State(Scope.Thread)
  public static class MyState {
    final HashFunction guavaHash = Hashing.murmur3_128(0);
    final MurmurHash3.LongPair pair = new MurmurHash3.LongPair();
  }

  @Benchmark
  public void guavaUnicode(MyState state) {
    for (String s : UNICODE_STRINGS) {
      state.guavaHash.hashString(s, StandardCharsets.UTF_8);
    }
  }

  @Benchmark
  public void guavaAscii(MyState state) {
    for (String s : ASCII_STRINGS) {
      state.guavaHash.hashString(s, StandardCharsets.UTF_8);
    }
  }

  @Benchmark
  public void murmurUnicodeBytes(MyState state) {
    for (String s : UNICODE_STRINGS) {
      byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
      MurmurHash3.murmurhash3_x64_128(bytes, 0, bytes.length, 0, state.pair);
    }
  }

  @Benchmark
  public void murmurAsciiBytes(MyState state) {
    for (String s : ASCII_STRINGS) {
      byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
      MurmurHash3.murmurhash3_x64_128(bytes, 0, bytes.length, 0, state.pair);
    }
  }

  @Benchmark
  public void murmurUnicodeString(MyState state) {
    for (String s : UNICODE_STRINGS) {
      MurmurHash3.murmurhash3_x64_128(s, 0, s.length(), 0, state.pair);
    }
  }

  @Benchmark
  public void murmurAsciiString(MyState state) {
    for (String s : ASCII_STRINGS) {
      MurmurHash3.murmurhash3_x64_128(s, 0, s.length(), 0, state.pair);
    }
  }

  // code used to generate random strings data

  public static void main(String[] args) {
    generateUnicodeStrings(64);
    generateAsciiStrings(128);
  }

  private static void generateUnicodeStrings(int maxLen) {
    StringBuilder res = new StringBuilder("  private static final String[] UNICODE_STRINGS = new String[]{\n");
    RandomStringsGenerator rsg = new RandomStringsGenerator();
    Random r = new Random();
    for (int i = 0; i < 100; i++) {
      int len = 1 + r.nextInt(maxLen - 1);
      String s = rsg.randomUnicode(len);
      res.append("    \"");
      res.append(StringEscapeUtils.escapeJava(s));
      res.append("\",\n");
    }
    res.append("  };");
    System.out.println(res);
  }

  private static void generateAsciiStrings(int maxLen) {
    RandomStringsGenerator rsg = new RandomStringsGenerator();
    Random random = new Random();
    StringBuilder res = new StringBuilder("  private static final String[] ASCII_STRINGS = new String[]{\n");
    for (int k = 0; k < 100; k++) {
      int len = 1 + random.nextInt(maxLen - 1);
      String s = rsg.randomAscii(len);
      res.append("    \"");
      res.append(StringEscapeUtils.escapeJava(s));
      res.append("\",\n");
    }
    res.append("  };");
    System.out.println(res);
  }

  private static final String[] UNICODE_STRINGS = new String[]{
      ":\u04F9\u001Dj\uD97D\uDC18\u07E9\u07FF\u01CD\uD92E\uDF3C\u001E\u03B9[\u001B\u0003\u0475lx\u05B7\uCB42\uDA5F\uDFD4\uDBFD\uDFEF\uD92A\uDD8B\uD9DA\uDD77\u67A3\uDA53\uDDF1\u3D3F\uD8DB\uDF3C\uDA3F\uDEF1\uC5BE\u2D91\u0016\uD975\uDD94\uD9C5\uDC22\u0164\u01F1\u01A2\u061C\u0005\u02D5\u1906\uF2FD\u07F2\u0756\uD93F\uDCC7\u04CD\u1FF6",
      "\uF594\uDA35\uDFD0\uDB0A\uDF2C\uDA64\uDDB0",
      "pn\u7E2Dq\u07DB\u0083\u019C_L\uDA0D\uDD56\u056A\uD8A1\uDD24\uD9A4\uDE76s\uC7F1\u00FE\u07A6\u4AB9\uD8F9\uDD60]",
      "*\uD8AD\uDCE2\u0655\u277E\u0A85\u05BE\u0338\u050D\u0410\u029C",
      "\u0019\u0506\uE23F\u20E6\u0190}l\u06C7\u5C9Eg\u05B0X\u059FG\uDAA5\uDD7F\u05AE\uDA03\uDE78\u506D\uD9F7\uDCB6\uDAB2\uDCC3\u8996\uDA7C\uDD90\u01D8\uD89A\uDD84\uDB65\uDCE4\uDBA9\uDC57\u7A47\u722E\u05A0\u07B7L5p\u0604\uDB9B\uDCBC\uE9D2\u9F99\u05FE\u0270\u07BB\u5AE3",
      "\uD86F\uDCDFS\u07975\uDB76\uDE17k\u04A8\uD8AA\uDFE1W\u01D01e\u2A14\u6B8FU$\u0274\u0596Hub\uD9EC\uDC1Cq\u04AC\u0489",
      "\uE548I\u21C1\uDA76\uDEBC}\uD904\uDEB7\uD9F7\uDDAC\u035D\u60BFW\uE889\uD945\uDEF1\u0B1B\u079C0\u02C3\uD82C\uDE4A\u001B\u03AC\u0087\u014F\u016Bf\uC370\u04C4\uDB3D\uDDE6",
      "\uBD47\u0140\u00C8\u259F\uD095\uD81C\uDED6\uB4C6l\uDBCB\uDF6F\uBB33t\u08A4\u03E9\uD95C\uDE1C\uDB0B\uDFC5\u0390\u188C\u115C\u0019\uD802\uDD92\u26BC\uDBAD\uDE11\u04F9\uDAE8\uDD8E(\uDBF8\uDEF1\uD9E5\uDCD1\uDA3F\uDD70\u0C65p\uD837\uDF12\u0006\u07ACX\u7B63\u0563\uDB2B\uDD21\uD851\uDE5F\u04B5\u02AA\u4C96f\u0E4F\u056D",
      "d\uDA40\uDE924f\u0015\uC865\u0698\uBA27\u07EA\uE386\uDA40\uDD45\u61F5\u0264\u000F\u0000\uD878\uDDEC\u04CB\u85BB\u04FBK\u0426\u06DC\u5AA7\u0523\uD9B9\uDD80\u00135\u0003\uAE49Eu\u02D1\uA886\uD80D\uDE01\u060Cfg\u2B8E\u01BA\u074EM\uD9D8\uDE2E",
      "\u0571\uD8EA\uDD44\uB509\u001E\u00D3\u0523\u0329\u001A\u013D\u0006\u0758\u0207\uDBB2\uDE36\u010E1m\u06DC\u8F94\u6792\uDA31\uDE40\u073A\u5685\uD83C\uDCDC\u03DD\u9845\u03E8\u544C",
      "\u044A\u0127\uC889\u03BB\u6FEE\f\u09B3\uD982\uDD0E\u033F\u5623\u3E40\u0001\u0019\u7BD4Xa\u73AA\u0362",
      ":\fZ\u0C76\u2D6D\u07C7\uDA0A\uDDBD\u7357\u07D3\u0010\uDB86\uDF4E\u02A9t\uFF0B\uAF39\uDB0B\uDDED\u0534\uD9BD\uDC6F\uD885\uDFC4\uDB94\uDECF\uD806\uDFBC\u00D40Vm\u033A\uDB58\uDC654",
      "\u033F\u0324\uDAC2\uDE22\uDA33\uDCD4\uDAED\uDC91\u9A2B\u035D\uB240\uE130&\uD818\uDDCE\uD723\u044Eg\uDA31\uDCF3p\uDA6C\uDC21\u0784\u0006\u061C\u4FAAm\uD8D2\uDD23E\u3218\uD8A7\uDD94\u04C6\uDBE6\uDC83\u000E\uB2AD\uDB59\uDE3C\uD87D\uDCC5\uD99E\uDF56\u9548\u0010\uD972\uDD1C\u6438\u069A\uDA53\uDF86\uD85B\uDD510>\u72FB\uD8F4\uDDF3\u1D6D\u068Do\u0B3C\u07E7\u030C\f\uDB69\uDFF5\u9012\uD89C\uDF16\uD852\uDF70}\uD88F\uDD18\uD936\uDC0E\u4EBD\uE36E\u0342_",
      "\u3836\u03D4TP\u00F8}Dt\u026A\uC0DC",
      "#'\u0358\uD9E5\uDC512m1\u5C0A\u3B07\uD8F0\uDD7C\uD833\uDE89dq&\u0012\u0012\uC0A3\u3B59\u0662\uD9FA\uDF89\uD942\uDDF6\uDB9D\uDC7D\u067F\f\u1F72\u05DD\uD95A\uDC32\uD976\uDD64\u0236\u4C94\u0492\u0083",
      "\u018E\u5F78\u07089\uDAB5\uDC3A\uDB47\uDE6B\u00C52\u04AE\u05CFs\u8DF3\uD825\uDCB2\uDBFA\uDE1C\u06DE\u0630\uDA3A\uDCE0\uD8AE\uDD3A\u01FE\u0017\u0144\uD71F2\u1ACB\u19B3\u97C7\uD940\uDFE6\u95C1\u7C65\u0319\u071C\uD9DA\uDF95\u0014\u748B\u07AB\u7694\u02919\u6A1F\uD8E8\uDC2B\u0348\u0005\u034BB`\u01CA",
      "\uDA3D\uDD3D\uD91D\uDC39\uD996\uDC42\uDAA6\uDE32\u02CF(`c\uDB83\uDE1E\u695B\u00AD",
      "\uD889\uDE2C\uC491\uD999\uDDB3\uDA55\uDEF8\b\u044Dz\u34DC\uDAB6\uDD5B",
      "<1\u07DF\uD848\uDC55T\uD98D\uDD64\u20F6\uE3C5\u026A\uF0C9 \u0256\u0015\u0436g\u0002\u0547\uCB87\u063Fp\uC2C4m\uE874\uF595\uA5D1A\u4469\u0597\u8255h\u07F5\u0512\u8D8C\uCA8F\uD9CC\uDEBD'\uC1DE\uDA0B\uDC17\uD24C\rN\u2738\u00D7\uE571\u001C\uDBE2\uDC37\uD87D\uDD38r\uD91B\uDFF6\u7260R\uDBB6\uDC02\u0569",
      "\u8AA9`\u0240C#\uDAB3\uDF3B\uDAA3\uDD8F\uD8ED\uDD70\u0176\u0420\u048E\u04D6\uDBA8\uDEC5\u50A0)\u0367s\u03CC\uDA54\uDEBD\uE8B3\uD9F9\uDEDE\u0362=\uD908\uDE4AY-h\uDB2C\uDE4BHN\u13B2\uFBBE\uFF30\u03C4\uDB44\uDD75\u04E5\u0003?h\u317C\u001F\uD9D7\uDE44\uE444e\uD88E\uDDD8\u3A65\uD894\uDF4F\uE07F\ff*\uD95D\uDE16rq\uC838\u0018P\u020A\uD8B9\uDF4B",
      "\uD91A\uDCC9\uDB03\uDC94k\u0016~\u001A\u0225\uD94C\uDD8E\u04E4\u001Fh\u1DF4\uD9F7\uDCF3\u059E\uD87C\uDC12\u0227q\u0016`\uD8EA\uDC9B",
      "\uD890\uDF7A\uD8ED\uDEF6\u1EB2\uDBD7\uDED8\u309F\u0715\uDA56\uDD8C\u065A%\u001EB|\u05C9\uDABE\uDE1C\uACD0\uD83A\uDCC3\u0632\u8A173\uDBCF\uDCDC\u01B9\uDA30\uDE21\u075A|/\uC4A7\u29C9\uD9BA\uDDAA\uD932\uDF19\u63F2i\uDB2A\uDFB3\u0519",
      "\uE389\u0002\uDA32\uDE35\u05B5\u12BD\uCFCE\uD956\uDEA6\uDA81\uDE06\u0597\u039AV\uD8C2\uDEDD\u3AF9\u000F\u0559s\u5E8B\uB904\u3DE5\u6BC2\uD908\uDD55F\u053E=xh\uDAE4\uDC2A\uDAFD\uDCD8\uD8B4\uDEE8",
      "\uD8ED\uDC4ECY\uB59E\uDBAF\uDD4A\uD10F\uDA41\uDCB6\uDBEE\uDE61\u6329Z}\u39FD\uC1C1c\u028E\uDA29\uDDB5\uDB1D\uDD21G\uDB26\uDC92X\u5850\uDB38\uDD94\u0441\uDBF4\uDE17\u05D5\uDB32\uDF4C\u05BD\u07AA\u001A\"\uDAA9\uDD24\uDA86\uDFBD\u3455\uD8A4\uDCE3Q\uDBAA\uDEAE:\uD050}}\uFF54\u9682}\u078B\uD928\uDE12\u0543\uD81D\uDCDC\u05ED\u0230",
      "\uDB06\uDCFD\u0376V\u07DD\u0005\u4983\uDAC3\uDD81\u01BF\uCA22\u05CCU\uD974\uDE04\u06B85\uDBC6\uDE33\u79C6\uDB01\uDE71S\u011F\uD8F5\uDC9D\u0088\uCC52\u0F6A\uC9BB5\u0489\uDBE1\uDC50\u001D\u03A4\u66FE\u0007\uDB29\uDE01\u030F\uDBEE\uDF03\uCA95\uD8C3\uDE45\uDBD5\uDF68\u3F05\u063B\u0610{\u22FD\uB006\uD82D\uDE77i\uD84E\uDE00\uD840\uDF6C\u071A\uD836\uDE76",
      "\u058F\u3B6A\u00FA\uD9E4\uDE6Cs\u01B3\uDBBC\uDCBDd\uD904\uDEE2\u0382\u5E07\u0C79\uDB04\uDDBA\u02A1\u7722\uBF48",
      "\u0266\u0512\u6973\u14B9\u3585\u8912\uDAA3\uDDCD\u3FA5\uDBA8\uDF204\uB6AA68\"\u8B50\u0016\uCE21\uDAA4\uDF32c\u0017\uD914\uDE57> \uC3A4\u07CD\u2605\u2380\u001Ff\u0001\u01BB\uDB17\uDDF5\u0427O\u06EE\uDA65\uDE44>",
      "\u06CFG>\u05B3\u02BA\u04DDL\u7201\uD8C9\uDFA0\uD97F\uDED2)\uDB36\uDF07\u2A59\u07BC\u9EC9qv\u2F19\u0620\u0007\u0712\u02EC\uDBF2\uDD42\u0006x\u3CA3\u2632\u000FY\u0015|\u068ES\u04EC\u3FE2\uDB32\uDD2D\uF087\u036F\u0531\uD925\uDC57;\uDBBF\uDC09P\u00EB",
      "9\u7EABe\uD9BA\uDE99\u0245\uDA16\uDED6\u078C\u0773\uDA69\uDDC0\u7A29\uD690\u5739v`\u0013\uDB1B\uDE52\u46EC\u07A0\u0683\u0011\uC0B6\uD9F1\uDC21\uDA06\uDFB2\uD0F1\u042E\u054B\u2CEE\uB116\u79D0\u06FF\u07AC+\u6257\uD8D0\uDFE3\u8F3A\u013A\uD871\uDF5E1\u0356\u32DA\u05FA\uDA10\uDF0C\u01C1\u55414\uDAF6\uDE95\u0017\uDA37\uDD15b",
      "\uD9E4\uDFB3\uDAEF\uDE26\u07FF\uDBE3\uDC48\u062E\u5EAE\uDA25\uDF23\uFEAC\u06BD\uD88A\uDF42\uD959\uDC49\u966D{\u6215_Q\u0607\u2701\u1C8D\u0235\u01E1RT\uD8AD\uDCF8S\uDB06\uDFB4\u069E\u01ED\u0590\u1C91\uD812\uDC48\u001C\u4E6B\u6483\u7674\u05E5\u266DI\uDA01\uDCC3\uD953\uDCDF\u03C0\u0332\uD95D\uDC58\uD8C3\uDFB9\uDAFD\uDCEF\uD0E5\uD96F\uDD32\uDB27\uDC63\u0133\u0017\uD998\uDC20\uCDDD\uDAF0\uDCACZ",
      "\u00D0Q?\uD8F9\uDDEE\uDBA3\uDC75\u0011\u0019r",
      "\uD8A9\uDFA7\u977B\u2099\uB9A7\u000E\u814A\u02BAs\u382A\uDBA7\uDCCD",
      "\uD98A\uDDF0\u0A15\uD8EA\uDC6C\uD790\u84F2\u0631\uDAA4\uDD48\u75CC\u17F1\u0411\u03BC\u8A0F\uDAD7\uDC82\uD872\uDE4D\uDA20\uDEFEa\uD80F\uDC3C\u067B\u02E3\uD846\uDCD1\uED44\u9203\uDB15\uDDB8\uF17B\u03C1\uD9C1\uDE5D\u07BC\u6314\uDAC1\uDCCF\u0607\u06CF\uD987\uDCDE\uD9FA\uDC7D\u0608\u054C\uDB80\uDD70\u0224\uCD06\uAA80\u01E1\u04AC\uA4C6\u8842",
      "j\uDAA9\uDDA77\u0300\uDAED\uDE89\uD8C3\uDCF0\u0797\uB19E\u0422\u864B\u0003\u0287\u07CD\uD3FA",
      "\u0534\uDA52\uDF9C\uDB5F\uDE6C\u078FT\uDB45\uDD81V\uD8C0\uDE0BV*\u05FC\uD9FC\uDF73\u1C85\uD8FE\uDD9D\uDABE\uDE03S<\u71C4a/\u030B\u02B8\u05E3\uD96F\uDF7A\u04A3\uEEB7#\uD85B\uDC62\u1B89\u00CB\u07EB",
      "\uDA5B\uDCE7\u9753\u1381\u0088\u0004\u3F4CB\uDAAD\uDFD8w\n\uBCF8Q\uDBC7\uDD12\u03EE\uDBF2\uDC1D\u0689\uD9A9\uDE06\uD8E9\uDF7A\u0102\uDA5F\uDF56\u0014o\uD9D2\uDD96\u1392\u850F\u52D4\u07E9\u0508\uD8DF\uDE55\uD852\uDC68\u9F86\u02203[\uD888\uDFBD\u06DE\uC79B\u0198\u05D3\uD81A\uDCFC\uCCDC\uDB53\uDC61\u01DC\u0413\u2336\uC8B7\u07BD\u074D\u00DC\u0328\u0013\u011F\uDA59\uDFEF\u00D7\uEAE2\u001D\uEE27\uD80A\uDEBA\u86D6\r\uFEFA\u0561",
      "\u0B5C\u0176\u039C7qy`M\u69A3B\u0491\u827A\u8215\uD913\uDEE6\u07AA\uD9B4\uDCC9\u2947\uFCC0\r-\u016D\uDB1D\uDD0D\uDBD3\uDC1DM\u0D4Bm\uE21E\uDBCC\uDE15\uDBF9\uDE1E\u07CF\n\u001C\u0003\u65F6\uE3814\u0569\u0486\uD81A\uDC2AP]\u023E\uDA6C\uDEFC\uDB72\uDEE9\u06C4\uDB41\uDECE\uD9A8\uDC97\u42C7\uD9DE\uDD68\uDBB4\uDF49\uDB13\uDC22\uD955\uDD94\u8B99",
      "\u03442e\uDA7B\uDF1C\u000F\u7457\u0001\u00DB\u03EDq\u1581",
      ">\uD8C5\uDE35\u0743\u3510\u6F08\u05F6\u1673\u024E\uD95B\uDE98I\uD7A0\uDA9B\uDE6F\u053E\u001D\uD88E\uDC85:\u81E9\u040A\uDB7B\uDD47\u45BA\u60F2",
      "\u097B\uDAF7\uDF92R\u03E3\u0573\uD92B\uDFEA\u02F8\u04FE\uD2E0E\uD883\uDD5D\u01E0\uD8D4\uDD27\u02CF\u0018\u84BB\u060A7\u2827\uDB7F\uDD51\u0553\uDB2A\uDEF4\u00B6\u04AF\u040B\u025A\uDA41\uDD55\u02A0\uB1FC\u0770\uD951\uDD17\uFFE8",
      "\u0012\uDBDB\uDCF7\u9C22\uAF89Z\\\u4E7C\u00F1\uD929\uDE39\u0647\uDB0E\uDC1C\u0492I\te\u0199\u000B\u019A\u9842\u00F8\u01ECy\u00172\uD931\uDFC6\uD8DE\uDD75\u394B\u03E8\uD391\u0530\uEB5B\uDAA1\uDC4E\uA160\u01C0\u1BA3\u0A2B\u02F0\u0448",
      "\uDA18\uDD44\uDB30\uDEA4\u31D8w\u0015\uD8CC\uDFE4",
      "\u000B\u03B8\u0012\u61C9\uD8DC\uDDD7\u03BF\u10BF\uD11B\uDBC7\uDEBE\u0014\u1507\u02D1\uC890\u02FA\uD925\uDDC1\u7F76\uD9DE\uDC17",
      "\uE032\uD8F9\uDCF6\u2D4D\u0656\u5933\uF403\u0096\uBAE9\u07E9\u61BC\uB60A\uC5E3\u1A6E\u03D4\u06B3\uC3F0\uD83C\uDF65\u0004\uD985\uDFA5\u06BE+<\u056E\u0406\u8EF8t\u074E\uDB66\uDD0D\u9CD1,\u54EA\uF490\u6299\u0360j",
      "\u030D\u0180\uD89A\uDF63\u0176\u8D8E\u0002R\u02E8\u03D5\uCDD3\uB25E\u249F\uD8EB\uDCF6\uD818\uDCE5\u0012\uD94E\uDF83\u04FC\u00DC\u0572\uB829\u0353\uF2AC\u04BAH\n\u03E3",
      "\uD924\uDD72^\uD822\uDFF2",
      "\u06B2;Zd_*\u0001\u024F\u0015\u0439\u0B87\uD8B9\uDF08\u0294\uD972\uDDF4E/\uD8B4\uDD12%\uDBFB\uDC77\uD329\uDA38\uDD77\uDB47\uDEA2\u05FE\u3279\uD9EE\uDE8E\u0652\uD85D\uDF76]\u0167\uD82D\uDDEE=\u0335\uDB36\uDF8C\uBB69\u063C\u01AB\u33B9\u0963\uDA0C\uDF0C\u01D3$\u0002}\u4F5E\u05E93\u7C79\uDA19\uDCD7\uC448U\u38D1e\u019D\uDAFF\uDC76\u5296\u1EAF\uDA6F\uDCD9\u6322r",
      "\u0685",
      "\u1ADE\u04F4\\\uD8E3\uDFC8A\u3E31\uA0C8",
      "\u046A\u940F\uD9F9\uDF1C\u025A\u0581\uD8AA\uDF59y\u0630\u075C\uB820\u371D\u128D\u6C33d\u0018\uF7FB\u0422\uC040\u06C6\uD95E\uDF33\uDA1B\uDE27\u001F\u0758#\uDB59\uDFB6\u2C67\u692F\u318E\uDB3C\uDEC2\u000EG;\uDB6F\uDE3E\uBAF6\uC5AC\uDBDB\uDC8E\u2A40\u056F\uD9CF\uDF69\u27C1\uD8DF\uDD70\uD8E5\uDCD2\u0513$\u064B\uDAD2\uDCEE\u27A4\u0407\uDA3A\uDEE6\uDA69\uDC9E\u02DC\u075D\u0786\uDAF5\uDE98\u6C20\u033C\u012B\u075E\uDB3F\uDF50\uDB9D\uDD47\uDA5D\uDC6CYY",
      "\uD9D4\uDFF8X\uA053K\uD849\uDFAB\u0195\u061B7\u147A\uDA14\uDCC5\uDB92\uDCD4\u1DED\u7284\uD9B5\uDF43\uD95E\uDC5D\uDAF9\uDE89\u04AD\u00EB\u023C\\s\uD844\uDC2A\u0088\u0005\u0003\u0484\uB717\u020F\u0677y\uD8B9\uDF10t\u4E09\uDAF8\uDD47\uA0D9\u05E7\uD8F0\uDE4A\u077D(\u001C\uD855\uDFB1\u07CB\u8F15\uD9B9\uDC61\u7A5D\u0010\uD92F\uDE70\u07B5\u0007\u30E58\u0518G\u00AE",
      "\uDAB1\uDC94\u05FBZ\u051E",
      "\uC3D4\u621B\u9E84\u3377\u6177\uFEC8\u7D07\uD99E\uDED1\u001E$cd\uD0DA\uE6D0\uC166\uD9E1\uDDEB!\u0006r\uD861\uDCE4\u0434\u264A\uD8A6\uDE90\u0313\u8557\u79FC\u00A5\uD879\uDFAE\u024Ff\uDBDD\uDC42\uFC7F7",
      "\uD842\uDEC56\u984AA\u0341\u037C\uDB0A\uDE27\u04BC\u0F78\uD854\uDC41\uD98B\uDCFFIU\u03FD\u0103K\u3F59\uB4CA\u0007\u0174\u05F3\u8833\u0745\u0580\u0208\uBE68\u05E3\uD927\uDF5D\u19A5\u0019\u00B4\uD921\uDFCCw\u04C9\u02CC\uDA36\uDC50\uD99E\uDE76\u021A3\u0013\uD9AB\uDC7E\u86D9C\uD8A2\uDEBF\u049B\u202C\u9D2E\u0388\"`\uFA6CW\u0234\uDA8C\uDFD1\u0335",
      "\uC68A\uD2BA\ng\uD8D6\uDC06?\uDBBA\uDE48\uD92A\uDF8B\u06F9\u86C5\uDA54\uDF33\uDB34\uDC4D\u023F\u07D0\u297D\u6B84\u8DBF*\uCF34\u0354\u0248\uDB79\uDFCB",
      "\uB251\u3B7B\u000F\uD982\uDFFF\uDACC\uDC1B\uDBCC\uDDA7\u0981\u055A]\u95128\uA452V\u054B\u5A6D b\uDAD8\uDC9F\u785A\uE579\u64E5\u063D\u0464\u02EB\u0019\uE2D3j\u35A1\u074B\uD8EB\uDF40\uD984\uDD10\u0DE4\uEDC4\uB79D\uDBA7\uDEB3\u38A3\uD86B\uDEBE\u03FE\u06E6\u84E75\u001E\u0230\u6F4B\u01E0\u001B\u06FD\uDA02\uDEEFyo\u5C0A\uDA51\uDF2D\u001C",
      "\uDBD3\uDF86",
      "\u001D\uD9E9\uDF93~T\u00F7\u02CF\uD902\uDEC8\u001A\u8522\u6AF5\uDACF\uDF62\uD83B\uDE89-\uCC63\u0004\u001B\uD95E\uDD82\uA916\uDB7C\uDF72\u00D1\uD8A7\uDC462\uDB7D\uDCB6\uD83B\uDD87\u000E\u071E\u0678\uA40E\uFB38\u79AB\u024Fj\uFF78\uDA3C\uDD1B\u0758\u1F53\u7775\uDA7F\uDFE0\u053C\u87CD\u01AD\u1437\uF07C\uDB64\uDFC9\u0467\u0476",
      "\u04C7%\uD854\uDEFA\uD873\uDDF6\u0111\u1F69\u0587\u0339\uD8A6\uDED6\u017F\uF4EB\uD83F\uDCA5\uB8CC`\uD898\uDF06\u02DD\uD926\uDC33^\u05F1\uD86B\uDE5F\u5B38\uD933\uDFB2\uDB77\uDE0A\uDA94\uDCBF\u000F\u026Ar\u4FB7\u37B8\uD824\uDF52\uD81B\uDFDD\"H\uD885\uDE46",
      "\uDB50\uDE99\u02D3\uDA74\uDF4B\u00071\u7AB4\uFFD8\u05F4x\uFD51\uD937\uDCBBg\u0006\u0013a\u03B8x\uD9A0\uDCE0\u022F\uDA09\uDF62~\u0437\u040A\u4FBB\uDAF3\uDE4C\uD9B2\uDD06=\uDA5F\uDEC4_f\uDB63\uDF78\uD8AC\uDFFD\u0004\uEF94\uDB45\uDF958\u5E41Y\u2A22\uDBDF\uDF20\uD807\uDEC6\uD94F\uDC91\u0781\u05D8\u0581-",
      "\u065D\uDA92\uDEDC\u0468S\u0139\u44B2\uDAA4\uDD32\u0003\u04D8\u312F\u0399\u071Fq\u561D\u0016\u06E7\u7131\uD9A1\uDE24\uEB73}\u06E8\u17BDW\u37CC\uD800\uDD29\uDA93\uDD02\u5656\u07C3\u0152\uDA9A\uDFEE\u5240\u00A3\uE508\uDADE\uDD27(\u035A\u8695\uD939\uDC1ExF\uD8CA\uDFDD\u0012\u27B0\u4CCF\t\u4129\u49EB\uD90F\uDDE0\u0311\uD56B\u71CB\uDA1C\uDE66,\uD434\uD85C\uDD93\u046B",
      "\u07B1\uD875\uDC94\u00FFH]\uDA59\uDD98\u06F1\u03F2\u0A94\u001F\u8AF5\u0234\uD89B\uDC05\u0003\u01A5\uD811\uDEADLY\uD80E\uDF19",
      "\u03A1\uB98F\u04D3\uD812\uDD68\u0758\u32B9\uD864\uDCBA\u02CD\u0557",
      "\u0001\u0010\u001BS\u043E\uF1C9\u07AC\u1146\uD96B\uDECC\uD22F\u07BAf\uD918\uDF7B\uDA80\uDE97_\uCDA3\u0554.\uD859\uDC87\u0104\u0005&\uFC9C\uE396\uD9E5\uDF2D\uD8D8\uDFA3\u001B\uD866\uDF44|\uC042\uDAC4\uDE89c\uDBAB\uDE79M\u074A\uDB31\uDFD3\u021E\u001B4\u0755\u036A\uD8F7\uDCC0\uDAFA\uDEA9\u6805\u0121\u0139\u0EDB\u053B\u4EEA\uDB6B\uDD9D",
      "\u0086\uAB91\uDA8B\uDF63]\uDABC\uDE8A\u8EDA\uD9D6\uDE0D\uF316\u0D60\u03B7\u02A4\uC6C1\u923C\u0263\uDB60\uDEBA\u0014\u05C7\u04B3\u0095\uDA24\uDDDD\uDB92\uDFF4`>\u07AA\u05B6\u000E\u00B5\u06D0\uFB9A\u0004\uD924\uDC21\uD86C\uDE4C\u04F1\u04B7\u01CE\u0207X\u9A8F\u0275\uE917\uDAFB\uDE5E\u0621\u0108\u595E\uDB39\uDF05\u049D\uFB80\u3285\u0000r\u039E\uD918\uDD85\"\uD9A9\uDE49\u0005\uD863\uDD10\u03E0",
      "\u0769\u0006\u0608\u0374\uD990\uDC98\u074B\uE544\u021F\u5A4D\u04E9\t\u38D57 \u04E4\u0559\u0373\u06B1\u02D0e\uD854\uDEA2\u3CEB\u3528\uDAA4\uDC39\uB5D0\u05F5\u05E7\u0576\u055E\uDBFF\uDDAA\uD9CA\uDE8C\uDB71\uDE68\u0014\u0093\u018D\uDBD0\uDCA6\uFEE8\f\uDA5B\uDE0E\u0368\b\u0417\uCDA5\u5B4B\u066E\u0D95I%\uD9B2\uDF27w\u001A\uDA72\uDF77\b\uDAB7\uDC7F\uDBC0\uDC4B",
      "\u0535\u0679,\uDA71\uDD8B\u459B\u051A?\u058F\u0018\uA0C1\u6DF2\u001B\u000El\u0516\uD817\uDC1A",
      "\u0583\uD8C0\uDF36\f;\u00B8N\uB521\uD99A\uDC6B\uD9D4\uDE21\u0643\u1D42\u9CD2",
      "\uF564\uDBE9\uDDC7\u04CE\u579B\uFCA8\u8C00F\uD99D\uDC1B\u68D2\u2359\u000F\u01EA\u06D3\uD84F\uDE65\uDA57\uDFA5\u001A6\uD856\uDF59\uD8C4\uDE14\u015C\uC2AA\uD808\uDC6B\uD845\uDEC9A\uD9B8\uDC43\u077A\uD991\uDD0E`\u0335\uD89B\uDDA2<\u039B\uDBB5\uDF68\u05A7\u364E\uA5EF[\uDACF\uDC4E\u06D9\uDBD9\uDD63\uD89C\uDFA6R\uDAB5\uDFAD\u03FA\u9971=\u001Ft\u01CF\uD99A\uDEC5",
      "\uB361\u8FC1a\u05CA'\uD8E6\uDC5Eo\u0742\u0159\u0003\u0004\u06BEx\u0593D\uDAD2\uDE96\u50E2q\u0016\uD8CA\uDFE3\uBF3A\uD649\u1FF6=$A\u3B62\u0298\u008D\uDA35\uDD9B\u0729\u3AFF\uC5FB\u04F6\u07B2i\u0C49\uD01Fh\u0087\uD88A\uDD6F\uDA20\uDD97\u07E4\uB1D5j\u2629\u2BE7\u6973wF?\u0FF2\u5370\uC54B\u3A67x\uDA0E\uDCAC",
      "\"\u34D4\uD934\uDFB3\u01E1\u0003\uDAA5\uDFAC\uD954\uDFF1\u0361\uD82C\uDD84\u0555\uD9C6\uDDB6e\u035E\uD896\uDFA0H\uDA46\uDD86\uD9E5\uDDA50G\u00D9r\u839BD\u8DBBp\u001F\uF444\uA22C\u6448s\u0019\uD994\uDCC4\uDAA3\uDF10\uBEDB_\uF8F1\u0652\uBD73\u6816;\u0011\u4FDE\u058A\u7037~\u05BA\u9E02\u2327`\uAFDC\uD9F8\uDF1B\u0567\u0775\uD8F1\uDCB2\u1FEE\u061F\uEB5F\u07B9\uDB07\uDDD8\u03AE\uFAB8",
      "\u0436L\u001F\u7D61\u000E\uFCE2\uDABF\uDF6D\uDB74\uDEB5\u07E8\u4555\uD8C9\uDC89?\uA140\uD92F\uDD2E\u0517g\u054E\u050C\uD9AE\uDEE7\uDB8D\uDF44 \u00A9\uD85F\uDC8E\u05CB\uD96A\uDFD9\u9270\u0689\u1130",
      "\uD869\uDCCB\u3DBA\uD954\uDE29eZ1\uD822\uDC66\u0414\u032E\u0799\uDB2C\uDCC6>\u035B\u1829\u0671\u07DB\uCD77\u0264_\uD9EB\uDEDB\u0E29\uD8EE\uDCA2\uC616\uE266\u04AD\uDB95\uDC30\uD968\uDCD3\u071A\u04FF\u02EB\u0017\u0615Bv\uF2B7\uDA29\uDF3E\uC1087=\uC801g\u07630\uD864\uDF9B\u64C4\u6921",
      "\u07F34F\uDBE8\uDF8E\u070F\uD8A6\uDF3B\uFD98\uDB6D\uDD70\u0244\u0019\u01A4\u001A\uDB20\uDCC3\u8394\uDA77\uDCD1\u176B\r\u03FB\u0D86\u0C611\uD8D0\uDE67\u05BD4\uDADA\uDF23\u018AWl\uDBB5\uDCAD\uDAD9\uDE62\u001C\u0384\u0010\u657F:\u01D88.\uC6D7\uB5E7\uDBB4\uDF14\u02EA\u45FF\u3C09\uD9C1\uDD10@\uDB3D\uDEA1\uDB28\uDE6F\u57F0\u9DAD\uDAD4\uDD4C\uD887\uDC1F\uD8E7\uDDD1",
      "\u02C1\u0652\uDA1E\uDCD2\uD9F7\uDF82\u0001\u03C6>\u21A5B\u0716\uDB4C\uDF4A\u050Ew\uCE1E\u0535\uD8DC\uDFB0\u5B02\u07BEv\u8820\u0225\u33FD\u0350\u0088\u0177\u85B1\u1DCB\u4924\u05C5\u02B8\u04D0\u0264\u0331\u0016.\u0003\u0754u\u8B8FxD\u00E4\u0101\uDAE5\uDEA2\u39DE%\uDB9A\uDE21\u0412\uD914\uDE30\u10EA\uD81C\uDE0D\u3983\u03CA",
      "\u06AAW\uDACF\uDF6B\u02A8\u0202\uC59A",
      "ac\uEF96\u0017\uD91C\uDD23\uFCD5\u061As\uF389\u8B0E\uD817\uDE28\uDA91\uDDFE\u21BE8i\u0435\uA685\u0413\u9FBB\u0002\u015A\u2CBD\uDA06\uDE50\u009E\uD93B\uDC59\u7409\u0770\u02391\u0014f\u03BD|\uDB57\uDDD4=\u0471\u01F7\"3\uD884\uDD90a E\u0201\u0570\uD944\uDFCE\f",
      "\\\uDA8F\uDD31\uD877\uDC6B\u06CB\u0019\uD895\uDC4A\uDA29\uDECA\u052C\u0697\u062D\uC066\u03C4u\uD905\uDF0Dv\u05F1\uDAE5\uDD3E\uA27A\u02C94\u0214i\u9AC6I\u0547r",
      "\u0097\u05427\u04D9\uA5941\u0572Y\u06F2\u0775\uD8D5\uDEC6\u2433\u0195[\u06AA\u0BBBm\uDAE2\uDFDF\uDAEE\uDE4B\uBE99\t\uC9D1\uD9EE\uDF02\u57B3\u6544\u0667",
      "\u0345\uD0FA~\uD8B9\uDFF9\uE6D9\u1E90\u05FC\u0017\u0012\t",
      "F\uD8B0\uDEC9\u050B\uDAEF\uDD3F\u0303\uDBBE\uDD38\uDA57\uDC8E%\u1D1F\uD872\uDCCA",
      "\uE4DCV\u05EF\uEBA9u\u0002\u0453\u01F3\uD9C3\uDDD6\u032Eg\uDA63\uDC5D\u393A\uDBD5\uDF3A\u0000o\b\uDB4F\uDF1D\u0522\u064C\u49E2\uDA70\uDF19\uD9C3\uDFE7n\uD93C\uDFD5\uD9B5\uDF77\u6953\uD5D8i\u026A\u2E75\uD913\uDD00a\uDB1B\uDD5C\uD890\uDC51\uF576Z",
      "\uCBA0\uDBE9\uDC2D\u0015\u027E\uDA44\uDEEBC\uDB30\uDCF2\uC528",
      "p\u2980\u04F9\uF6C9\uDB67\uDDC6\uDBDF\uDF2E\uAA82\u2A6F7\uD97B\uDF59\uDA0A\uDEC9h\u024F\uD8C1\uDE3D\u05C7O",
      "r\uD8C8\uDC25\uDADE\uDC74\u0651\uD896\uDC02\f\uDAB5\uDDDD\u05A2\u89BB\uB766\u9D81\u0462\uE666\uA415\uDAA3\uDEF0\\_g\uD848\uDD20o\u0001\uDB79\uDC8C\u001B\u0006\uDB5C\uDCDF\uD9DF\uDDC2)\uACCA\uDBB9\uDEEC\uD0EE\u0002\uFE6B\uFEFE0",
      "a&\uEDA0\u04FA\uD5EA\uA335I7]\u000B35\u0015\u01A1\u6690\u074Cq\u51D8",
      "\uD808\uDFFC\u029D\u0005\uDB20\uDCD2M\uA76A\u0402\u0002<\uFD2A\u076F\u0124?f\u02F4\uBB79\u013D\uDB17\uDC19\u014B\uDBA1\uDE6A\uD9AA\uDF7C\uD97B\uDC44\u2752R\uD88B\uDE56j\u34C9\u1217\u01BD\u00F2\uD905\uDE51\u03E3\u34AF\u4FD3\uD8D3\uDCFEJ&\uDAEA\uDD62\u3405\u974F\u07EA\u9F02\uDAED\uDD82\u03A4Kyq\u96C7:\u0131\uFB24\uDA20\uDFCB\uD893\uDC5D0\uD1D8\u0299b\u0017Q",
      "\uD988\uDC2A\u9EDE\uD9F6\uDDBD\uA743\u0001\u0012\u0222\uDA68\uDC4B3\u6BD67\u0DCB\u0018\uFFA1\u70F0\uDB9F\uDE19\u03BB\u00B3\uDB22\uDFC3\u4E19\uDB4F\uDFCB\u2496\u957E\uDA27\uDF00\u01C3\uD990\uDE3Ff\uD867\uDF83\uD8C6\uDD08",
      "\u14E87\uBE5D\uD92A\uDD8Ct\uDAB4\uDF62\uB0AB\uDA7D\uDCA7,\uD933\uDD3E\uF262\u0A5F\u6C46\u062C\uDB2F\uDCB3\u0634\u04A4\u433A\u0578?\u427A\u066D9\u00C8\u04AC\uCCD1Y\u071F\uD8AB\uDF97\u0DFC\uD88E\uDF5B\u053A\u5F1C\uDBB0\uDC16\u0751 \u9FE5\uD910\uDEB9\u04E1\u3549\u0017\uDB9B\uDC3E\uD9BB\uDDDB\u8D18\u0556\u07D3\u06E3\u02E4E\uC2AF\uD99F\uDF56\u04B2@4\u016F\uDA51\uDCC12\u030C\u0591\u53CE\u0716\uD904\uDCA3\u95F4",
      "\uD4DB\uA340Q",
      "\u07088\u07AB\u00A8^,\u0561%\uDBEE\uDF16\u0019q\uA2C7\u6A6B\uDA33\uDC4C1:\uDA08\uDC2F2\u0002\u00B9\u229E\u07E3\u913E\uD4CA\uD9C3\uDE01\u0632\u0457\u0004\u39D8C\u111A\uDABA\uDC7D\uD9C2\uDE90\u6D8D",
      "\u67E1\u087B\u000E\u02DB\uB5D1\u0540\uAA43\uD950\uDF98\u4161\u5CA8\uD9FC\uDD42\uD992\uDE57\uD8E7\uDCDE\u7256h\u04A0",
      "\u0430\u8F21\u9E6Bn\u05B3\uA764\u0718\u0003\u04E2\u0367}]",
      "\uDA74\uDE15,n\u4F29\u807C\u0313\uDAE5\uDD48\uD86F\uDDC4\uCA80\u063E\uF122\u06AC\u0002O\t\u017E\u8F22\u07ED\u01F6\uDB03\uDEAD5\uDA9E\uDFF5\u03D5\u2DE5\u30B5>\u073E\u5D0Fj\u2309\u0184B\uD9D1\uDCFF\u65BEH\u22C0\u031B\uD931\uDE3A%O\u0010\u0013\uDBF3\uDEDCw\u0015\u331C\uD8BF\uDD05\uDB7C\uDC39%\uED97\uD925\uDF99\u3A5B\u016F",
      "\u23D1L\uD8B9\uDCDC\uD979\uDD5C\uCEF5[\uA794\u0287\u7507\u001A;\uCAAD\u0521w\uFD7C\u059C\u1670\u0192n\u0015\uA6B3\u02AC\u0493\u0015\uFDE2\uD80C\uDD2E\u03FF\u0000",
      "E\u040A\uD8BE\uDF82\uDB7F\uDF5B\u0686\bqG\u0666p\uA384\uDA55\uDE11\u0610\r8*\uD89D\uDF3C\u0014\uD856\uDF31\uDA7A\uDED3P\u0012\uD9AA\uDE82D\u0018\u4AEC\u9F67\uDA97\uDEBF\u0017w\u0453\u05C4\uD82A\uDDF7\uD857\uDC06I\u00B7\u071F\u8FCF\uD94D\uDE40\uDAD4\uDCFA\uD91A\uDEAB?%|\uCB83\u05F1\u21254e\u04F0\u031E",
      "/\u6377B\u3B7F\uC1DE\u7408~\u0371\uCCE4\u0731\u0428\uDBEF\uDC5C\uDB0F\uDFA5\uA311\uD98C\uDCB7\u000F\u00C5\u061Eb\u0839\u1403i\u0276\u001A\u06D8\u6D16\u04DF\u500C\u175Eb\t",
      "58\u0134\uDB0A\uDFAE\u0F70u\uDB5E\uDDBF\u03CB\uD806\uDCDA\\\uBBD9%\uF268\u626E\u6504\uDB6D\uDE61\uB139\u1F7A&\uD808\uDD0A\u001A\u07CF)\u05C3R\uDBD6\uDE1B\u072C\uDB18\uDC03\u1C5A\uBEF9]\u6B95\uDA23\uDE42<\uD9F6\uDC37U\u034F\u0E41\uD881\uDCF8\u7EE2\u079Fb\u00F9\u04F4\u8C73\u0589\u000E\u8F64\uD924\uDF67y\u0130\uB3C3-\u0690\uDBA3\uDFEE\uDAD7\uDDB1{\u0158\uDB83\uDD95\u04CF\u52F1\u4E9B",
      "\u060E\uD835\uDDC8\u06AE\u0562\u253D\uDAC9\uDD31\u0797W\u050C\u06AD\uDBB5\uDCBB\u04C6\uD8D3\uDCC2\u00B9\u8AA2\u0014\u02F1\u00C4\uDBE6\uDF62\u0F65\uB9EA?\uDBFF\uDF3C\uD9B3\uDF6E\uDA8D\uDC32\u045E\u03BB\u07E5\u750B\u0288\u00ED\u0018\uDB56\uDE78\u0007\uDBE8\uDD30g\uCD25",
      "\u6B77\u03D4\uD986\uDF2B\u48C3\uD8A3\uDE5A\u192F\u8B82\u0397\uD92D\uDDEAO\u009D\u0015o\u38EA\uDB28\uDFB8!\uDAFB\uDFA6\uDA72\uDE44\u070C\uDA99\uDDFBU\uD998\uDEF7\u8E88\u05C1\u058F\u011F\u0000\uDB74\uDE1C\uDBA2\uDE8B\u194A\uA30Dm{\u0017\uD870\uDC29\uDA0E\uDEB5\uD9D9\uDEB4\u0252\u7173\uF652\uBAC1\uDA12\uDD43\u0002\u5CD1\uD8A9\uDCFA\uD9CE\uDD71\uDBBC\uDC9AjV\uD90B\uDEE8\uDA30\uDD77",
  };
  private static final String[] ASCII_STRINGS = new String[]{
      "nRWvEPejWg5QR4wJtPCJbypY1CjPDqD2eHH1mFBjkBdmb41xIK5KOGVM4v5Lm8OImum0Ep1rxn1g",
      "hFSVcryWml6mT6cSqTNhTONBwy8swT13qgwW1",
      "8SuJGSu70VMEhHiggoQmLioL6gkPe3vCRUMDriB4PVBm1JcCg8Pm0d4nrNY5cbc0IxC3bvqsX6CdBiWKosSnH4rvod8tuk38T",
      "WOFXXv0V7bf6SfsMfGHrBxmNqCdwbmkUNKgJJLvMkqmbGFE",
      "LgdujUxgKRG6cvIkXboIbbqoJcXnfqkMt4JufFRXvPndy4B2eio7BUd14JrF5k7FcT3m7Q4upbxLKDkGg0su7Ot0BthsNGOuX0oQ",
      "BSuBeEiwesJO7DMFD2cRdVKy8O4edOBBk6ucrHlmkLjiE7LjdcnHmpISj6lLj6opMQ2GeWscICTgJNHl2vcQWG6lE2lmxK6bfXLbFQpBLoTd7PrfmI7CWqjpicM0I",
      "i8gQRJhXUFIdDJkBDPIVioYe35ON8LGBnjPwBqroTvTSjJBdrJHHSJLyJIrmKYdEoqI5cwWuDiK",
      "xcQNNhi7NVtBmkRmqCB5Yp1CMJxFWr1KSGvfU3e3T0X3SkrFruqm6sQM7M18DibyyIPcuHkh8fns18mgFDeYx3j2vjTCPKR4T",
      "tlRmVO8HPThR6OEftjygNiBb",
      "2K7QBC74wIMlKl8x3OUEIq1cnLvW58vS0Hbx0KtR8SiO2NRlYofsKRqkmxHbIfQ8plgxocHbr7CklKDLmB08pJx",
      "1NEycfsg1yM2Lgecbtxs8P6wp4NI6KK1jTfestID2etrhLxXmrRpHkB",
      "PyovhlLc6TdB7ytsqp8G7UoJ08jo8SocHFlOHywhMrC",
      "07CU60mtHIvvH0iO0wKf4YxsVK6",
      "MigpTcOD7PXhl6B70WyoKN5n",
      "LCoKPM7rpxpkwIrM1JMb3LTBPoP2WYOIKNTJwXfyHnLp7NcoITOvcobiuJupW3WqUrE",
      "pfp8wdKkGWT5SSpKNGhhVRJrK7vkndRLpMrstDGR06N8xBQ2JX6mVroQyjxY3nul2cv23tdcui8GQwkGNnLk8poGRRPuoFIyb2wHhiNRFJhx2InwowRCs",
      "qVv8mpvC3RnYJMYmckFVCkwtjKuI5BkemJG",
      "7rs12igPDOBylns65hjuypib8ECpqjU0w1QgLJ4vQNFTsyFDMRnNuUGn4ivp0",
      "ComJXS5IJqqTMP0dTbIjMjXsJ3OHpXCyusEBjYpyUPxicNGOetnfbb0NhbsRcK5eMfMDnpjk57rSKYTLB3yVgmHvH3Ws4lWbrm5YijXqDXphrY",
      "45m8SVcshxEgInP1l6B8Tm8WPB86eBTUWgCtV15WK",
      "ew0SiXFqLb7nfdsjsd2kqJf25Bq1pOGQnf",
      "cT4VWSPFelDwfpGIv6tXf3wjg5NHy145Th",
      "WByJIfXn2rNputktu2YX8224BQKguXXbc7CU72SpEQVIKYExBOi6K",
      "tTqO4wtiuECOIoVUSJMtx3WdyctYugc40m050qPK4PF2H6n05qMqpi1601scMPNoT2UV2LD4wH0kDE",
      "el5qEPNQnTLB3VDVgQds1xVs3YgtoP43GLjw4iQLL4hYvt0nvgKSvk4W6mNefM3VTRHQUueHKLGYtvTGb84IcTgxdwgVQLTS1mwf8yheqsH5ufSrNr0CQ",
      "pLLoB22VNXj7fUNJgT7NI35",
      "bY",
      "C5MGwcfSpRrQNlBDo82so1tToHRTgQ0TowE1fiJfB3sO5KJSXowfY3IiwB0m2jDQRFj2jbsoiVTcw6mt",
      "mP0sh41Jv0WDy7wupV1yig6cM8OoK4prtCVg4jx5p1wLNE3JQ7B82XetDpxMrDKUSPnjeSiC0hq0Rgmd40DCWWFWmHN",
      "P1D0mCUMyuwYwsSsLUJuIDcnuxqcqqKIvYHC7xcHR0CjQy0wTL6224hWOSbXWlm0iQgklo",
      "IiKUHr4gB1K3n1Xm6SVfOtXxMrHbdBvy3MJDwO2L10vLI1MrT7vJFbOD6JTIFFVj70DjsBUptkKO1oCNkNsW",
      "5O03CxcPdsvcLlvc6UduwoHCYPtpPWW5cGYjMUQ3xjCgwpX",
      "gW4VNtjQTQfd5MkX70EnqPOldSCEMeQNEr",
      "EWjuHemqfqoTUhhDiDTmh7Kep0mlwmxBfyuRfQnjpNwCgJboOx5rq0m2IU1Xc4qwyG",
      "8IrBHmk3U3NJfOesMoI0M0hxv7tENugW4NFIMxt6DKODNX46geGgbq0Ih1owQCmUOBMqkbomIftc0oDF5QYSeR5Mqd8d",
      "mKGmW3ooglQ6PcJ50q6eErvxbW5OnhmVELmdFLUTimnBtxc",
      "uV8ShnPe34hw6YX4ymyE3wmUlKGnw2kHj0b2TYMSuRlj1WyKK2OyK1MKEwqWHGLFR1ym40We7FlipDwcd2XfCsoXkVXNVY41",
      "phvfWGFpLTqdLsRLQSbTVY6jlblBbG2YoTKhrn6wFMTqmJkn0k3Ni26Uinl7PudrtGYmvFYg7lrVgsx1hu",
      "B2fk3s4n2tVj7sUxDQlW64ufJPN0yOr3FfsrBOCwkioyggr5l5QuGqUYe18i8b",
      "k6gXcy4GNHiH8FCNsxGfIs72NnwFK71i72mn0kPWnxwo",
      "28hH0voMDOH42TWBIWNhp7LJlr5kYbjBt6ylEEuOrE",
      "jmv6NX4fIu3mskTR2ST3LcrgF4XYcgTrSVh1wSM4iL",
      "MfGCj61Phyym2PxBe6g0F7qhHfC7ksOQ5eiFNB6QDkxFOp2BXiLF5sEUs80Ge1PWs76lPJge5C5dFvHPl4V7hpNHm7UJs8wsCxeI",
      "DSe4V4n7iDIvgw3fOtreQImimpElhdOLpyp0yKb2RyRUov8QV5JXP5Pd6EM8B8H8p8fOsk4NGDLG2DOmUisiCHmojseDfJqR7kXSxkOFLyj",
      "6IgIk13TuYKCDBNvQf3FCcH7rNPc06Tu6pHieSxi5UH2S",
      "WYD3Y6sM7I5sXRpxpMLqNehi7LkCHXKNXufou86KmJgC44NNWcwSYCDcGRXUD72XNwdNSVy52pWxwTcRix027uJCqb3N22Bs3r5iyTSvI6tWgWCBWhTrFl",
      "72FB6vXFcf4Cswv0UrQ1f25OTFBneknYEjVtPUbnIhrQJ",
      "dg6wcnQSi8Kq78Jb",
      "OJBGcwlgQrwfKlt6BTsBeR5QOUJvdoYHwwCEfrFKWmNWw2O1LwuH1GPXPbV828kmlDlnG5mQ1xuTKk2FBmjkxViwVh586TGpDN",
      "d3vGx4D06GkdJneS6wC1V0kqhTB7",
      "Oc7R6Nw2dsoTiFE4qCQf5f2M2g",
      "q4b31GgJw3weVgeM0Wh",
      "covSyMiVh0DHm22lMhwdPN67N7duXsgkuG3yKC3BPpXPbYfSsVq21OfnYLXk4hXgcOuId7T4se4jX6QW5gMtIsHkeowQNOQl7o",
      "OSxT7jxE7oSwF01Ji2dXK3ygsmYSWw0FBBtmS2IE4c3Ecov4oqTuUKj3d4gSIP5vw3mpxsNrqpRhMtxeDg6JNykrV7syN1hmfQvUGJkjGKXCuNOKmYVNHJuOu5",
      "vfvCxg00518iOoDggHrxL4TxDfCnBRg2J4Cl7wf3gDetpNb8me6pvcX7bPsRyXjrNtVC0CVur0JHC02SKgT74Vo",
      "S6dRINq2KHqdol7rRFEhX0VJiQv7mIjWYEFMeQxcWkxjopfjpiUH0UqiuDrGGbeJXuQvyuFu",
      "sjxs6i21mcvQR07h61VQfrHOF0wn0sFrioIObGJ4jBTrrXVwoSWGEeTf0pnVfETb352m4u1nhfTQN25SreFq3LcRgiEe6JMlsT",
      "sREslG1vVYvqyRFNFU4tGQ6M5rynyi6Fjx76c3UCiB48en4brCPnmqCVrLi51Q4mDtyXkhrIF0XqQwGdlyQOd2Uv2FDDiOCosfiviMi",
      "pkNvJ6W0R76dEMuRCRLyRn6EY76WM8HLHitIxqh6XsN86O6P",
      "qr1WL4OsdLuXTRn4mwkiG3HmU7rGRs25UXnIC0ENOUMnImW0N4CHuswLX2S2",
      "UbtHFBJKSpEHuuk1k1dYci08pWqYupRGfFvxr34Ef3jTsR3jKeVhRkPsSWbXTBk3gjnxX4SnI8eblu2KJyG0I43Ym7PKhJe1ynpo1qtVsGx6xbxDf2EucTIq",
      "8re4jlW8otrT1FNHvHnc42mBUS1lHUD",
      "D3PR4hbXcPvchNxWPFPOj53torgdf55UMRFWCyHIurYbuhhQST18BTb7xM55OhROtytlXdoNfNTCjRlsgFvBBo7jmv5oYPeBhu2JQMfMI78ptiMVPI5MX",
      "dOvD68HoSIMGQxv0TvDGQPjLeiyvGL424",
      "WXIloI4dBJ2RceuKKqWgfNQqULJ5yg3WEcwWngvHUJdp0jEviyCgfrYST6RiRjdyiUEBvWUO",
      "ugUmomv1PxsOypePkeYbYoH3w1NTD8IjOvWUYJyO4PUII2WrET7hjXHp1T0g6cRDJiM5UM7uuOR8cqCKm57tsFei2c7pl76VWOTVheEm",
      "eEJHDk0BC",
      "iTXLS5JslllIqCc8DMTI420suiTrGJg7PdwrWUByR",
      "bov2yM1OiKc5U8ErdPmtHhOEEOGVoexbMwLMWxt3V4VvMl7fWHgYUqHcV3Bw61sdldW0QvXmcsBiCE71GTJ2WT0py1f7VdEPYqHHvph5D6MOyXk5ltyPBJgTbTr",
      "XtWJBpBu13rJheQvvkOEqdUJKbCkH6J2FfSdCEOgbVBmDdms5JPjLVdSi1kN07MgxvR3us0qgoYFd2Rf7gL4QThbQm6pQI2M7EmLfxeViEU3byep65J8lD",
      "vXjYer0bBpehEXiUmskoyQvsqUgCbLv3Rd2jeN1WtvqQMjNQp75oOBhhGlWUNge0Spu0usU6o0QNE2gmTrG2CDePpQl3T0VHWufq2u6BtKbwsSlwD0D0jLEWHo8T",
      "pv8E7H3unfGVfEGcrMY2f5xk1XlBgudkqHHFl4glIrRvofCt5QbxXPoGTnwfoBlSy4jnhWUJD718FnmV7FtTCWHTLoriwrBxTh43gPJpNnG3Q2wfiKkGH",
      "lvXjFylQq1JPSDQW",
      "SsEDSC3R2y3UJhffnP3hhemm1mfdC",
      "DRUR0W4T7tw5QhD02IxWqSsJcrk6MOEwHFrrPrQLJiHYJB8rTrSttdUjcRXisoo6hCP",
      "PnBWsM2OjCIkQeUJTPSty0KY23Fd6ifpKM3I6prkc5knwrgudE67d7fcrhoErSldUWlvcf5IQG64ceL5kJcv3hSWHhq2oYO4ySOhLVUhE",
      "0GI615LPeSpdCCMWgd0KQq3WY8NNEP2U6545QdE88VhwmtNut8hXvTRd",
      "dWYF0leRYTTkJ8lIeL3kKh3bKOQXPDhw6fmNvM5m36EDm6fE1VeB8UK0p0yEh3ue",
      "DNRk1m0sdfxEWQCxwk8niUn",
      "8cRJio74K5CrCVhN21enM8QrrspJ6noNsfk4MJFJku4VwTnuVV8Oht0UiNV",
      "LMPqHTGsuOMTJL0isldHqHfIYYwU2YyhheR68CQJ7rBQiqfP8yMvkHlBtyjdVwwXgpdeD",
      "Qiej7C",
      "DctXetbRPn85MmuN6diInyohFykJUwhQX1Su0He8",
      "P23h2DFNrflLLMpCe7xWm57ug31hnsckpWOp5qDxQT",
      "RicW4h2BLdHGUSL7IxEIkcQXfqCH32y8fd1dwrSmUbFoXhVN44ggPLW5wK7Mus8o8JO",
      "CD2ck2xX05WCDKM7sbl7N5xKQwGJkgRytoTH5WqTEeHYXTMJgRUfpDP",
      "PXMW5cMj4lRE2qqRwJE3YOjglHJ8R1cRV1hk0JiEbYuCoSbv4wWuwyrPuF1FBeWd8BXcJTvJK",
      "TCsCKCOeUbJY17Xk7eBbqPdUvprN8Drt21RrVS4DYjFIcnpqLTdEtg6vJBpPksTxghyyc2tr64kVhqv6tLGy6f3",
      "eUo2F1lJPGLLoXYyqR0OJ2G1GFwWRNU3u24k",
      "V2NuxbLEp08xjrnIgFrnxstwbmtP6U6hvd6SE3P0RNK5m3bqp1XiENlJmo6otrDmsDx3YwGtD2GMIgoyHqjLWv8m8875x72UOKv140e",
      "QnrJhxFcHInXfwo7IqqMMniKSsl6o0d4U3cVsfvHEDrUCC",
      "3dNY6F5IICtOM1qlQ5HBPioWDJmqXILTEe6DJf",
      "Yf8WoS3gppFVGHW",
      "yBpbmDMY",
      "fy7qQ3gcXE00E75tnsFoPo2FlVI3JSIoYYvBwSOHV1NGCd0GfOkP0pE",
      "e0gLjjYjD5lv3hntUltCyljRDKo0D08eE8exSyt8nToLs1Iee5VdkHDo1QOERV4BI2T6RN6kpTbwxXSryv5HUMbDpCcq37",
      "S1QyFDOdnGFyOHrb2VTqNgOVJeObt5vRHyHBK5uHsCyCiIgGgyb6b8wdBjV4C7NnwWL6V8YULpcRcew6j6FBSQtoyPvHmSWNrJX8KdeFeFkfbIXNYy2mchER4",
      "2dGIR6DT0rnliXmGNdI5",
      "27uMoMWSyGK7sCIUH0Cr8X0xXFJC6cKOdDJeBUDe4oXX5dQsec7827mLgEpuCWviLYoYN07xSfDQGJUMWFi6k4NhB3K7X8uYmRBtoGtJgQvfc2u",
      "JIQgSoXtK8m8rvEC3",
  };
}
