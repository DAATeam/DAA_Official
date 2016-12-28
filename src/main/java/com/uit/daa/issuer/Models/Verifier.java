/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

/**
 *
 * @author nguyenduyy
 */

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.uit.daa.issuer.Models.Authenticator.EcDaaSignature;
import com.uit.daa.issuer.Models.Issuer.IssuerPublicKey;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import iaik.security.ec.math.curve.ECPoint;
import java.util.Arrays;

/**
 * Class containing the Verifier ECDAA functions
 * @author manudrijvers 
 *
 */

public class Verifier {
	private BNCurve curve;
	public static String JSON_REVOCATION_LIST = "RogueList";
	public static String JSON_REVOCATION_LIST_ENTRY = "RogueListEntry";
	
	public Verifier(BNCurve curve) {
		this.curve = curve;
	}
	
	/**
	 * Verifies an ECDAA signature
	 * @param signature an ECDAA signature
	 * @param appId The AppID (i.e. https-URL of TrustFacets object)
	 * @param pk the Issuer public key
	 * @param revocationList the list of revoked private keys
	 * @return true iff the signature is valid
	 * @throws NoSuchAlgorithmException
	 */
	public boolean verify(EcDaaSignature signature, String appId, IssuerPublicKey pk, Set<BigInteger> revocationList) throws NoSuchAlgorithmException {
		boolean success = true;
		
		// Check that a, b, c, d are in G1
		success &= this.curve.isInG1(signature.r);
		success &= this.curve.isInG1(signature.s);
		success &= this.curve.isInG1(signature.t);
		success &= this.curve.isInG1(signature.w);

		// Check that this is not the trivial credential (1, 1, 1, 1)
		success &= !this.curve.isIdentityG1(signature.r);
		
		// Verify that c2, s2 proves SPK{(sk): w = s^sk}(krd, appId)
		success &= signature.c2.equals(this.curve.hashModOrder(
				this.curve.point1ToBytes(
						signature.s.multiplyPoint(signature.s2).subtractPoint(signature.w.multiplyPoint(signature.c2))),
				this.curve.point1ToBytes(signature.s),
				this.curve.point1ToBytes(signature.w),
				appId.getBytes(),
				this.curve.hash(signature.krd)));
		
		// Verify credential
		success &= this.curve.pair(signature.r, pk.Y).equals(this.curve.pair(signature.s, this.curve.getG2()));
		success &= this.curve.pair(signature.t, this.curve.getG2()).equals(
				this.curve.pair(signature.r.clone().addPoint(signature.w), pk.X));

		// Perform revocation check
		if(revocationList != null) {
			for(BigInteger sk : revocationList) {
				success &= !signature.s.clone().multiplyPoint(sk).equals(signature.w);
			}
		}
		
		return success;
	}
        
        public boolean verifyWrt(byte[] message, byte[] session,EcDaaSignature signature, String appId, IssuerPublicKey pk, Set<BigInteger> revocationList) throws NoSuchAlgorithmException {
            BigInteger l = this.curve.hashModOrder(message);
            //ECPoint a  = this.curve.getG1().multiplyPoint(l);
            BigInteger h = this.curve.hashModOrder(session);
            //ECPoint r = a.multiplyPoint(h);
            boolean success = true;
        //    success &= (signature.r.getCoordinate().getX().getField().getCardinality() == r.getCoordinate().getX().getField().getCardinality());
        //    success &= (signature.r.getCoordinate().getY().getField().getCardinality() == r.getCoordinate().getY().getField().getCardinality());
            signature.w = signature.w.multiplyPoint(l);
            if(success){
                return verify(signature, appId, pk, revocationList);
            }
            else return success;
        }
	
	/**
	 * Turns a revocation list as a set of big integers into a JSON object
	 * @param revocationList The revocation list as a Set<BigInteger>
	 * @param curve The curve used
	 * @return The revocation list as a JSON object
	 */
	public static String revocationListToJson(Set<BigInteger> revocationList, BNCurve curve) {
		StringBuilder sb = new StringBuilder();
		Base64.Encoder encoder = Base64.getUrlEncoder();
		
		sb.append("{\"" + JSON_REVOCATION_LIST + "\":[");
		
		StringJoiner sj = new StringJoiner(",");
		for(BigInteger revoked : revocationList) {
			sj.add("{\"" + JSON_REVOCATION_LIST_ENTRY + "\":\"" + encoder.encodeToString(curve.bigIntegerToB(revoked)) + "\"}");
		}
		sb.append(sj.toString());
		sb.append("]}");
		
		return sb.toString();
	}
	
	/**
	 * Turns a revocation list as JSON object into a set of big integers
	 * @param json the revocation list as a JSON object
	 * @param curve the curve used
	 * @return the revocation list as a Set<BigInteger>
	 */
	public static Set<BigInteger> revocationListFromJson(String json, BNCurve curve) {
		Base64.Decoder decoder = Base64.getUrlDecoder();

		JsonArray object = new JsonParser().parse(json).getAsJsonObject().getAsJsonArray(JSON_REVOCATION_LIST);
		Set<BigInteger> rl = new HashSet<BigInteger>(object.size());
		for(JsonElement element : object) {
			rl.add(curve.bigIntegerFromB(decoder.decode(element.getAsJsonObject().get(JSON_REVOCATION_LIST_ENTRY).getAsString())));
		}
		return rl;
	}
        public boolean link(EcDaaSignature sig1, EcDaaSignature sig2){
            if(sig1.nym!= null && sig2.nym != null){
                if(Arrays.equals(sig1.nym, sig2.nym)){
                    return true;
                }
                else return false;
            }
            else return false;
        }
}
