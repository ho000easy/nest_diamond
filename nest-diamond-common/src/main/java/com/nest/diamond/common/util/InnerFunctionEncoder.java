package com.nest.diamond.common.util;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.web3j.abi.DefaultFunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.web3j.abi.Utils.staticStructNestedPublicFieldsFlatList;

public class InnerFunctionEncoder extends DefaultFunctionEncoder {
    public static String buildMethodID(final Function function) {
        final List<Type> parameters = function.getInputParameters();

        final String methodSignature = buildMethodSignature(function.getName(), parameters);
        return buildMethodId(methodSignature);
    }

    public static String buildMethodID(String method) {
        return buildMethodId(method);
    }


    public static List<Type> decodeInputData(String rawInput, final Function function) {
        return decodeInputData(rawInput, function.getInputParameters());
    }

    public static List<Type> decodeInputData(String rawInput, List<Type> typeList) {
        String _inputData = Numeric.containsHexPrefix(rawInput) ? rawInput.substring(10) : rawInput;
        List<TypeReference<Type>> inputTypes = typeList.stream().map(type -> {
            try {
                if(type instanceof DynamicStruct){
                    return (TypeReference<Type>) TypeReference.create(type.getClass());
                }
                return (TypeReference<Type>) TypeReference.makeTypeReference(type.getTypeAsString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        return FunctionReturnDecoder.decode(_inputData, inputTypes);
    }

    public static List<TypeReference<Type>> contractMethodParams(Class<? extends Type>... types) {
        List<TypeReference<Type>> typeReferenceList = Lists.newArrayList();
        for (Class<? extends Type> type : types) {
            typeReferenceList.add(createType(type));
        }
        return typeReferenceList;
    }

    public static TypeReference<Type> createType(Class<? extends Type> typeClazz) {
        String solidityType = typeClazz.getSimpleName().toLowerCase();
        try {
            TypeReference<Type> uintTR = (TypeReference<Type>) TypeReference.makeTypeReference(solidityType);
            return uintTR;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

    }

    public static List<Type> decodeInputData(String inputData,
                                             String methodName,
                                             List<TypeReference<?>> outputParameters) {
        Function function = new Function(methodName,
                Collections.<Type>emptyList(),
                outputParameters
        );
        List<Type> result = FunctionReturnDecoder.decode(
                inputData.substring(10),
                function.getOutputParameters());
        return result;
    }

    public String encodeParametersV2(final List<Type> parameters) {
        return encodeParametersV2(parameters, new StringBuilder());
    }

    private static String encodeParametersV2(
            final List<Type> parameters, final StringBuilder result) {

        int dynamicDataOffset = getLengthV2(parameters) * Type.MAX_BYTE_LENGTH;
        final StringBuilder dynamicData = new StringBuilder();

        for (Type parameter : parameters) {
            final String encodedValue = TypeEncoder.encode(parameter);

            if (TypeEncoder.isDynamic(parameter)) {
                final String encodedDataOffset =
                        TypeEncoder.encodeNumeric(new Uint(BigInteger.valueOf(dynamicDataOffset)));
                result.append(encodedDataOffset);
                dynamicData.append(encodedValue);
                dynamicDataOffset += encodedValue.length() >> 1;
            } else {
                result.append(encodedValue);
            }
        }
        result.append(dynamicData);

        return result.toString();
    }

    // TODO web3j此方法有bug，没有计算嵌套对象的真实长度
    @SneakyThrows
    private static int getLengthV2(final List<Type> parameters) {
        int count = 0;
        for (final Type type : parameters) {
            if (type instanceof StaticArray
                    && StaticStruct.class.isAssignableFrom(
                    ((StaticArray) type).getComponentType())) {
                count +=
                        staticStructNestedPublicFieldsFlatList(
                                ((StaticArray) type).getComponentType())
                                .size()
                                * ((StaticArray) type).getValue().size();
            } else if (type instanceof StaticArray
                    && DynamicStruct.class.isAssignableFrom(
                    ((StaticArray) type).getComponentType())) {
                count++;
            } else if (type instanceof StaticArray) {
//                List<Field> fieldList = staticStructNestedPublicFieldsFlatList((Class<Type>) type.getClass());
                List<Type> fieldObjs = ((StaticArray) type).getValue();
                count += getLengthV2(fieldObjs);
            } else {
                count++;
            }
        }
        return count;
    }

}