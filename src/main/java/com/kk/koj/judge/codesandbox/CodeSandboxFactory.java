package com.kk.koj.judge.codesandbox;

import com.kk.koj.judge.codesandbox.impl.ExampleCodeSandbox;
import com.kk.koj.judge.codesandbox.impl.RemoteCodeSandbox;
import com.kk.koj.judge.codesandbox.impl.ThirdPartyCodeSandbox;

/**
 * 使用工厂模式
 * @author KK
 * @version 1.0
 */
public class CodeSandboxFactory {


    public static CodeSandbox getCodeSandbox(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }
    }
}
