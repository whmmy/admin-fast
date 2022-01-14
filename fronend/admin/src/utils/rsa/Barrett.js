// BarrettMu, a class for performing Barrett modular reduction computations in
// JavaScript.
//
// Requires BigInt.js.
//
// Copyright 2004-2005 David Shapiro.
//
// You may use, re-use, abuse, copy, and modify this code to your liking, but
// please keep this header.
//
// Thanks!
// 
// Dave Shapiro
// dave@ohdave.com 
import BigInt from "./BigInt"

function BarrettMu(m)
{
	
	this.modulus = BigInt.biCopy(m);
	this.k = BigInt.biHighIndex(this.modulus) + 1;
	var b2k = new BigInt.BigInt();
	b2k.digits[2 * this.k] = 1; // b2k = b^(2k)
	this.mu = BigInt.biDivide(b2k, this.modulus);
	this.bkplus1 = new BigInt.BigInt();
	this.bkplus1.digits[ this.k + 1] = 1; // bkplus1 = b^(k+1)
	this.modulo = BarrettMu_modulo;
	this.multiplyMod = BarrettMu_multiplyMod;
	this.powMod = BarrettMu_powMod;
}

function BarrettMu_modulo(x)
{
	var q1 = BigInt.biDivideByRadixPower(x, this.k - 1);
	var q2 = BigInt.biMultiply(q1, this.mu);
	var q3 = BigInt.biDivideByRadixPower(q2, this.k + 1);
	var r1 = BigInt.biModuloByRadixPower(x, this.k + 1);
	var r2term = BigInt.biMultiply(q3, this.modulus);
	var r2 = BigInt.biModuloByRadixPower(r2term, this.k + 1);
	var r = BigInt.biSubtract(r1, r2);
	if (r.isNeg) {
		r = BigInt.biAdd(r, this.bkplus1);
	}
	var rgtem = BigInt.biCompare(r, this.modulus) >= 0;
	while (rgtem) {
		r = BigInt.biSubtract(r, this.modulus);
		rgtem = BigInt.biCompare(r, this.modulus) >= 0;
	}
	return r;
}

function BarrettMu_multiplyMod(x, y)
{
	/*
	x = this.modulo(x);
	y = this.modulo(y);
	*/
	var xy = BigInt.biMultiply(x, y);
	return this.modulo(xy);
}

function BarrettMu_powMod(x, y)
{
	var result = new BigInt.BigInt();
	result.digits[0] = 1;
	var a = x;
	var k = y;
	while (true) {
		if ((k.digits[0] & 1) != 0) result = this.multiplyMod(result, a);
		k = BigInt.biShiftRight(k, 1);
		if (k.digits[0] == 0 && BigInt.biHighIndex(k) == 0) break;
		a = this.multiplyMod(a, a);
	}
	return result;
}

export default {BarrettMu}
