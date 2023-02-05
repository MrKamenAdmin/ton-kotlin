package org.ton.asm

import org.ton.asm.appactions.*
import org.ton.asm.appaddr.*
import org.ton.asm.appconfig.*
import org.ton.asm.appcrypto.*
import org.ton.asm.appcurrency.LDGRAMS
import org.ton.asm.appcurrency.LDVARINT16
import org.ton.asm.appcurrency.STGRAMS
import org.ton.asm.appcurrency.STVARINT16
import org.ton.asm.appgas.ACCEPT
import org.ton.asm.appgas.COMMIT
import org.ton.asm.appgas.SETGASLIMIT
import org.ton.asm.appglobal.GETGLOB
import org.ton.asm.appglobal.GETGLOBVAR
import org.ton.asm.appglobal.SETGLOB
import org.ton.asm.appglobal.SETGLOBVAR
import org.ton.asm.appmisc.CDATASIZE
import org.ton.asm.appmisc.CDATASIZEQ
import org.ton.asm.appmisc.SDATASIZE
import org.ton.asm.appmisc.SDATASIZEQ
import org.ton.asm.apprnd.ADDRAND
import org.ton.asm.apprnd.RAND
import org.ton.asm.apprnd.RANDU256
import org.ton.asm.apprnd.SETRAND
import org.ton.asm.arithmbasic.*
import org.ton.asm.arithmdiv.*
import org.ton.asm.arithmlogical.*
import org.ton.asm.arithmquiet.*
import org.ton.asm.cellbuild.*
import org.ton.asm.cellparse.*
import org.ton.asm.codepage.SETCP
import org.ton.asm.codepage.SETCP0
import org.ton.asm.codepage.SETCPX
import org.ton.asm.codepage.SETCP_SPECIAL
import org.ton.asm.compareint.*
import org.ton.asm.compareother.*
import org.ton.asm.constdata.*
import org.ton.asm.constint.*
import org.ton.asm.contbasic.*
import org.ton.asm.contconditional.*
import org.ton.asm.contcreate.BLESS
import org.ton.asm.contcreate.BLESSARGS
import org.ton.asm.contcreate.BLESSNUMARGS
import org.ton.asm.contcreate.BLESSVARARGS
import org.ton.asm.contdict.CALLDICT
import org.ton.asm.contdict.CALLDICT_LONG
import org.ton.asm.contdict.JMPDICT
import org.ton.asm.contdict.PREPAREDICT
import org.ton.asm.contloops.*
import org.ton.asm.contregisters.*
import org.ton.asm.contstack.*
import org.ton.asm.debug.DEBUG
import org.ton.asm.debug.DEBUGSTR
import org.ton.asm.debug.DUMP
import org.ton.asm.debug.DUMPSTK
import org.ton.asm.dictcreate.DICTEMPTY
import org.ton.asm.dictcreate.NEWDICT
import org.ton.asm.dictdelete.*
import org.ton.asm.dictget.*
import org.ton.asm.dictmayberef.*
import org.ton.asm.dictmin.*
import org.ton.asm.dictnext.*
import org.ton.asm.dictprefix.PFXDICTADD
import org.ton.asm.dictprefix.PFXDICTDEL
import org.ton.asm.dictprefix.PFXDICTREPLACE
import org.ton.asm.dictprefix.PFXDICTSET
import org.ton.asm.dictserial.*
import org.ton.asm.dictset.*
import org.ton.asm.dictsetbuilder.*
import org.ton.asm.dictspecial.*
import org.ton.asm.dictsub.*
import org.ton.asm.exceptions.*
import org.ton.asm.stackbasic.*
import org.ton.asm.stackcomplex.*
import org.ton.asm.tuple.*
import org.ton.tlb.TlbCombinator
import org.ton.tlb.providers.TlbCombinatorProvider

public interface AsmInstruction {
    public companion object : TlbCombinatorProvider<AsmInstruction> by AsmInstructionTlbCombinator
}

private object AsmInstructionTlbCombinator : TlbCombinator<AsmInstruction>(
    AsmInstruction::class,
    NOP::class to NOP,
    SWAP::class to SWAP,
    XCHG_0I::class to XCHG_0I,
    XCHG_IJ::class to XCHG_IJ,
    XCHG_0I_LONG::class to XCHG_0I_LONG,
    XCHG_1I::class to XCHG_1I,
    PUSH::class to PUSH,
    DUP::class to DUP,
    OVER::class to OVER,
    POP::class to POP,
    DROP::class to DROP,
    NIP::class to NIP,
    XCHG3::class to XCHG3,
    XCHG2::class to XCHG2,
    XCPU::class to XCPU,
    PUXC::class to PUXC,
    PUSH2::class to PUSH2,
    XCHG3_ALT::class to XCHG3_ALT,
    XC2PU::class to XC2PU,
    XCPUXC::class to XCPUXC,
    XCPU2::class to XCPU2,
    PUXC2::class to PUXC2,
    PUXCPU::class to PUXCPU,
    PU2XC::class to PU2XC,
    PUSH3::class to PUSH3,
    BLKSWAP::class to BLKSWAP,
    ROT2::class to ROT2,
    ROLL::class to ROLL,
    ROLLREV::class to ROLLREV,
    PUSH_LONG::class to PUSH_LONG,
    POP_LONG::class to POP_LONG,
    ROT::class to ROT,
    ROTREV::class to ROTREV,
    SWAP2::class to SWAP2,
    DROP2::class to DROP2,
    DUP2::class to DUP2,
    OVER2::class to OVER2,
    REVERSE::class to REVERSE,
    BLKDROP::class to BLKDROP,
    BLKPUSH::class to BLKPUSH,
    PICK::class to PICK,
    ROLLX::class to ROLLX,
    ROLLREVX::class to ROLLREVX,
    BLKSWX::class to BLKSWX,
    REVX::class to REVX,
    DROPX::class to DROPX,
    TUCK::class to TUCK,
    XCHGX::class to XCHGX,
    DEPTH::class to DEPTH,
    CHKDEPTH::class to CHKDEPTH,
    ONLYTOPX::class to ONLYTOPX,
    ONLYX::class to ONLYX,
    BLKDROP2::class to BLKDROP2,
    NULL::class to NULL,
    ISNULL::class to ISNULL,
    TUPLE::class to TUPLE,
    NIL::class to NIL,
    SINGLE::class to SINGLE,
    PAIR::class to PAIR,
    TRIPLE::class to TRIPLE,
    INDEX::class to INDEX,
    FIRST::class to FIRST,
    SECOND::class to SECOND,
    THIRD::class to THIRD,
    UNTUPLE::class to UNTUPLE,
    UNSINGLE::class to UNSINGLE,
    UNPAIR::class to UNPAIR,
    UNTRIPLE::class to UNTRIPLE,
    UNPACKFIRST::class to UNPACKFIRST,
    CHKTUPLE::class to CHKTUPLE,
    EXPLODE::class to EXPLODE,
    SETINDEX::class to SETINDEX,
    SETFIRST::class to SETFIRST,
    SETSECOND::class to SETSECOND,
    SETTHIRD::class to SETTHIRD,
    INDEXQ::class to INDEXQ,
    FIRSTQ::class to FIRSTQ,
    SECONDQ::class to SECONDQ,
    THIRDQ::class to THIRDQ,
    SETINDEXQ::class to SETINDEXQ,
    SETFIRSTQ::class to SETFIRSTQ,
    SETSECONDQ::class to SETSECONDQ,
    SETTHIRDQ::class to SETTHIRDQ,
    TUPLEVAR::class to TUPLEVAR,
    INDEXVAR::class to INDEXVAR,
    UNTUPLEVAR::class to UNTUPLEVAR,
    UNPACKFIRSTVAR::class to UNPACKFIRSTVAR,
    EXPLODEVAR::class to EXPLODEVAR,
    SETINDEXVAR::class to SETINDEXVAR,
    INDEXVARQ::class to INDEXVARQ,
    SETINDEXVARQ::class to SETINDEXVARQ,
    TLEN::class to TLEN,
    QTLEN::class to QTLEN,
    ISTUPLE::class to ISTUPLE,
    LAST::class to LAST,
    TPUSH::class to TPUSH,
    TPOP::class to TPOP,
    NULLSWAPIF::class to NULLSWAPIF,
    NULLSWAPIFNOT::class to NULLSWAPIFNOT,
    NULLROTRIF::class to NULLROTRIF,
    NULLROTRIFNOT::class to NULLROTRIFNOT,
    NULLSWAPIF2::class to NULLSWAPIF2,
    NULLSWAPIFNOT2::class to NULLSWAPIFNOT2,
    NULLROTRIF2::class to NULLROTRIF2,
    NULLROTRIFNOT2::class to NULLROTRIFNOT2,
    INDEX2::class to INDEX2,
    CADR::class to CADR,
    CDDR::class to CDDR,
    INDEX3::class to INDEX3,
    CADDR::class to CADDR,
    CDDDR::class to CDDDR,
    PUSHINT_4::class to PUSHINT_4,
    ZERO::class to ZERO,
    ONE::class to ONE,
    TWO::class to TWO,
    TEN::class to TEN,
    TRUE::class to TRUE,
    PUSHINT_8::class to PUSHINT_8,
    PUSHINT_16::class to PUSHINT_16,
    PUSHINT_LONG::class to PUSHINT_LONG,
    PUSHPOW2::class to PUSHPOW2,
    PUSHNAN::class to PUSHNAN,
    PUSHPOW2DEC::class to PUSHPOW2DEC,
    PUSHNEGPOW2::class to PUSHNEGPOW2,
    PUSHREF::class to PUSHREF,
    PUSHREFSLICE::class to PUSHREFSLICE,
    PUSHREFCONT::class to PUSHREFCONT,
    PUSHSLICE::class to PUSHSLICE,
    PUSHSLICE_REFS::class to PUSHSLICE_REFS,
    PUSHSLICE_LONG::class to PUSHSLICE_LONG,
    PUSHCONT::class to PUSHCONT,
    PUSHCONT_SHORT::class to PUSHCONT_SHORT,
    ADD::class to ADD,
    SUB::class to SUB,
    SUBR::class to SUBR,
    NEGATE::class to NEGATE,
    INC::class to INC,
    DEC::class to DEC,
    ADDCONST::class to ADDCONST,
    MULCONST::class to MULCONST,
    MUL::class to MUL,
    DIV_BASE::class to DIV_BASE,
    DIV::class to DIV,
    DIVR::class to DIVR,
    DIVC::class to DIVC,
    MOD::class to MOD,
    DIVMOD::class to DIVMOD,
    DIVMODR::class to DIVMODR,
    DIVMODC::class to DIVMODC,
    RSHIFTR_VAR::class to RSHIFTR_VAR,
    RSHIFTC_VAR::class to RSHIFTC_VAR,
    RSHIFTR::class to RSHIFTR,
    RSHIFTC::class to RSHIFTC,
    MODPOW2::class to MODPOW2,
    MULDIV::class to MULDIV,
    MULDIVR::class to MULDIVR,
    MULDIVMOD::class to MULDIVMOD,
    MULRSHIFT_VAR::class to MULRSHIFT_VAR,
    MULRSHIFTR_VAR::class to MULRSHIFTR_VAR,
    MULRSHIFTC_VAR::class to MULRSHIFTC_VAR,
    MULRSHIFT::class to MULRSHIFT,
    MULRSHIFTR::class to MULRSHIFTR,
    MULRSHIFTC::class to MULRSHIFTC,
    LSHIFTDIV_VAR::class to LSHIFTDIV_VAR,
    LSHIFTDIVR_VAR::class to LSHIFTDIVR_VAR,
    LSHIFTDIVC_VAR::class to LSHIFTDIVC_VAR,
    LSHIFTDIV::class to LSHIFTDIV,
    LSHIFTDIVR::class to LSHIFTDIVR,
    LSHIFTDIVC::class to LSHIFTDIVC,
    LSHIFT::class to LSHIFT,
    RSHIFT::class to RSHIFT,
    LSHIFT_VAR::class to LSHIFT_VAR,
    RSHIFT_VAR::class to RSHIFT_VAR,
    POW2::class to POW2,
    AND::class to AND,
    OR::class to OR,
    XOR::class to XOR,
    NOT::class to NOT,
    FITS::class to FITS,
    CHKBOOL::class to CHKBOOL,
    UFITS::class to UFITS,
    CHKBIT::class to CHKBIT,
    FITSX::class to FITSX,
    UFITSX::class to UFITSX,
    BITSIZE::class to BITSIZE,
    UBITSIZE::class to UBITSIZE,
    MIN::class to MIN,
    MAX::class to MAX,
    MINMAX::class to MINMAX,
    ABS::class to ABS,
    QADD::class to QADD,
    QSUB::class to QSUB,
    QSUBR::class to QSUBR,
    QNEGATE::class to QNEGATE,
    QINC::class to QINC,
    QDEC::class to QDEC,
    QMUL::class to QMUL,
    QDIV::class to QDIV,
    QDIVR::class to QDIVR,
    QDIVC::class to QDIVC,
    QMOD::class to QMOD,
    QDIVMOD::class to QDIVMOD,
    QDIVMODR::class to QDIVMODR,
    QDIVMODC::class to QDIVMODC,
    QMULDIVR::class to QMULDIVR,
    QMULDIVMOD::class to QMULDIVMOD,
    QLSHIFT::class to QLSHIFT,
    QRSHIFT::class to QRSHIFT,
    QPOW2::class to QPOW2,
    QAND::class to QAND,
    QOR::class to QOR,
    QXOR::class to QXOR,
    QNOT::class to QNOT,
    QFITS::class to QFITS,
    QUFITS::class to QUFITS,
    QFITSX::class to QFITSX,
    QUFITSX::class to QUFITSX,
    SGN::class to SGN,
    LESS::class to LESS,
    EQUAL::class to EQUAL,
    LEQ::class to LEQ,
    GREATER::class to GREATER,
    NEQ::class to NEQ,
    GEQ::class to GEQ,
    CMP::class to CMP,
    EQINT::class to EQINT,
    ISZERO::class to ISZERO,
    LESSINT::class to LESSINT,
    ISNEG::class to ISNEG,
    ISNPOS::class to ISNPOS,
    GTINT::class to GTINT,
    ISPOS::class to ISPOS,
    ISNNEG::class to ISNNEG,
    NEQINT::class to NEQINT,
    ISNAN::class to ISNAN,
    CHKNAN::class to CHKNAN,
    SEMPTY::class to SEMPTY,
    SDEMPTY::class to SDEMPTY,
    SREMPTY::class to SREMPTY,
    SDFIRST::class to SDFIRST,
    SDLEXCMP::class to SDLEXCMP,
    SDEQ::class to SDEQ,
    SDPFX::class to SDPFX,
    SDPFXREV::class to SDPFXREV,
    SDPPFX::class to SDPPFX,
    SDPPFXREV::class to SDPPFXREV,
    SDSFX::class to SDSFX,
    SDSFXREV::class to SDSFXREV,
    SDPSFX::class to SDPSFX,
    SDPSFXREV::class to SDPSFXREV,
    SDCNTLEAD0::class to SDCNTLEAD0,
    SDCNTLEAD1::class to SDCNTLEAD1,
    SDCNTTRAIL0::class to SDCNTTRAIL0,
    SDCNTTRAIL1::class to SDCNTTRAIL1,
    NEWC::class to NEWC,
    ENDC::class to ENDC,
    STI::class to STI,
    STU::class to STU,
    STREF::class to STREF,
    STBREFR::class to STBREFR,
    STSLICE::class to STSLICE,
    STIX::class to STIX,
    STUX::class to STUX,
    STIXR::class to STIXR,
    STUXR::class to STUXR,
    STIXQ::class to STIXQ,
    STUXQ::class to STUXQ,
    STIXRQ::class to STIXRQ,
    STUXRQ::class to STUXRQ,
    STI_ALT::class to STI_ALT,
    STU_ALT::class to STU_ALT,
    STIR::class to STIR,
    STUR::class to STUR,
    STIQ::class to STIQ,
    STUQ::class to STUQ,
    STIRQ::class to STIRQ,
    STURQ::class to STURQ,
    STREF_ALT::class to STREF_ALT,
    STBREF::class to STBREF,
    STSLICE_ALT::class to STSLICE_ALT,
    STB::class to STB,
    STREFR::class to STREFR,
    STBREFR_ALT::class to STBREFR_ALT,
    STSLICER::class to STSLICER,
    STBR::class to STBR,
    STREFQ::class to STREFQ,
    STBREFQ::class to STBREFQ,
    STSLICEQ::class to STSLICEQ,
    STBQ::class to STBQ,
    STREFRQ::class to STREFRQ,
    STBREFRQ::class to STBREFRQ,
    STSLICERQ::class to STSLICERQ,
    STBRQ::class to STBRQ,
    STREFCONST::class to STREFCONST,
    STREF2CONST::class to STREF2CONST,
    ENDXC::class to ENDXC,
    STILE4::class to STILE4,
    STULE4::class to STULE4,
    STILE8::class to STILE8,
    STULE8::class to STULE8,
    BDEPTH::class to BDEPTH,
    BBITS::class to BBITS,
    BREFS::class to BREFS,
    BBITREFS::class to BBITREFS,
    BREMBITS::class to BREMBITS,
    BREMREFS::class to BREMREFS,
    BREMBITREFS::class to BREMBITREFS,
    BCHKBITS::class to BCHKBITS,
    BCHKBITS_VAR::class to BCHKBITS_VAR,
    BCHKREFS::class to BCHKREFS,
    BCHKBITREFS::class to BCHKBITREFS,
    BCHKBITSQ::class to BCHKBITSQ,
    BCHKBITSQ_VAR::class to BCHKBITSQ_VAR,
    BCHKREFSQ::class to BCHKREFSQ,
    BCHKBITREFSQ::class to BCHKBITREFSQ,
    STZEROES::class to STZEROES,
    STONES::class to STONES,
    STSAME::class to STSAME,
    STSLICECONST::class to STSLICECONST,
    STZERO::class to STZERO,
    STONE::class to STONE,
    CTOS::class to CTOS,
    ENDS::class to ENDS,
    LDI::class to LDI,
    LDU::class to LDU,
    LDREF::class to LDREF,
    LDREFRTOS::class to LDREFRTOS,
    LDSLICE::class to LDSLICE,
    LDIX::class to LDIX,
    LDUX::class to LDUX,
    PLDIX::class to PLDIX,
    PLDUX::class to PLDUX,
    LDIXQ::class to LDIXQ,
    LDUXQ::class to LDUXQ,
    PLDIXQ::class to PLDIXQ,
    PLDUXQ::class to PLDUXQ,
    LDI_ALT::class to LDI_ALT,
    LDU_ALT::class to LDU_ALT,
    PLDI::class to PLDI,
    PLDU::class to PLDU,
    LDIQ::class to LDIQ,
    LDUQ::class to LDUQ,
    PLDIQ::class to PLDIQ,
    PLDUQ::class to PLDUQ,
    PLDUZ::class to PLDUZ,
    LDSLICEX::class to LDSLICEX,
    PLDSLICEX::class to PLDSLICEX,
    LDSLICEXQ::class to LDSLICEXQ,
    PLDSLICEXQ::class to PLDSLICEXQ,
    LDSLICE_ALT::class to LDSLICE_ALT,
    PLDSLICE::class to PLDSLICE,
    LDSLICEQ::class to LDSLICEQ,
    PLDSLICEQ::class to PLDSLICEQ,
    SDCUTFIRST::class to SDCUTFIRST,
    SDSKIPFIRST::class to SDSKIPFIRST,
    SDCUTLAST::class to SDCUTLAST,
    SDSKIPLAST::class to SDSKIPLAST,
    SDSUBSTR::class to SDSUBSTR,
    SDBEGINSX::class to SDBEGINSX,
    SDBEGINSXQ::class to SDBEGINSXQ,
    SDBEGINS::class to SDBEGINS,
    SDBEGINSQ::class to SDBEGINSQ,
    SCUTFIRST::class to SCUTFIRST,
    SSKIPFIRST::class to SSKIPFIRST,
    SCUTLAST::class to SCUTLAST,
    SSKIPLAST::class to SSKIPLAST,
    SUBSLICE::class to SUBSLICE,
    SPLIT::class to SPLIT,
    SPLITQ::class to SPLITQ,
    XCTOS::class to XCTOS,
    XLOAD::class to XLOAD,
    XLOADQ::class to XLOADQ,
    SCHKBITS::class to SCHKBITS,
    SCHKREFS::class to SCHKREFS,
    SCHKBITREFS::class to SCHKBITREFS,
    SCHKBITSQ::class to SCHKBITSQ,
    SCHKREFSQ::class to SCHKREFSQ,
    SCHKBITREFSQ::class to SCHKBITREFSQ,
    PLDREFVAR::class to PLDREFVAR,
    SBITS::class to SBITS,
    SREFS::class to SREFS,
    SBITREFS::class to SBITREFS,
    PLDREFIDX::class to PLDREFIDX,
    PLDREF::class to PLDREF,
    LDILE4::class to LDILE4,
    LDULE4::class to LDULE4,
    LDILE8::class to LDILE8,
    LDULE8::class to LDULE8,
    PLDILE4::class to PLDILE4,
    PLDULE4::class to PLDULE4,
    PLDILE8::class to PLDILE8,
    PLDULE8::class to PLDULE8,
    LDILE4Q::class to LDILE4Q,
    LDULE4Q::class to LDULE4Q,
    LDILE8Q::class to LDILE8Q,
    LDULE8Q::class to LDULE8Q,
    PLDILE4Q::class to PLDILE4Q,
    PLDULE4Q::class to PLDULE4Q,
    PLDILE8Q::class to PLDILE8Q,
    PLDULE8Q::class to PLDULE8Q,
    LDZEROES::class to LDZEROES,
    LDONES::class to LDONES,
    LDSAME::class to LDSAME,
    SDEPTH::class to SDEPTH,
    CDEPTH::class to CDEPTH,
    EXECUTE::class to EXECUTE,
    JMPX::class to JMPX,
    CALLXARGS::class to CALLXARGS,
    CALLXARGS_VAR::class to CALLXARGS_VAR,
    JMPXARGS::class to JMPXARGS,
    RETARGS::class to RETARGS,
    RET::class to RET,
    RETALT::class to RETALT,
    BRANCH::class to BRANCH,
    CALLCC::class to CALLCC,
    JMPXDATA::class to JMPXDATA,
    CALLCCARGS::class to CALLCCARGS,
    CALLXVARARGS::class to CALLXVARARGS,
    RETVARARGS::class to RETVARARGS,
    JMPXVARARGS::class to JMPXVARARGS,
    CALLCCVARARGS::class to CALLCCVARARGS,
    CALLREF::class to CALLREF,
    JMPREF::class to JMPREF,
    JMPREFDATA::class to JMPREFDATA,
    RETDATA::class to RETDATA,
    IFRET::class to IFRET,
    IFNOTRET::class to IFNOTRET,
    IF::class to IF,
    IFNOT::class to IFNOT,
    IFJMP::class to IFJMP,
    IFNOTJMP::class to IFNOTJMP,
    IFELSE::class to IFELSE,
    IFREF::class to IFREF,
    IFNOTREF::class to IFNOTREF,
    IFJMPREF::class to IFJMPREF,
    IFNOTJMPREF::class to IFNOTJMPREF,
    CONDSEL::class to CONDSEL,
    CONDSELCHK::class to CONDSELCHK,
    IFRETALT::class to IFRETALT,
    IFNOTRETALT::class to IFNOTRETALT,
    IFREFELSE::class to IFREFELSE,
    IFELSEREF::class to IFELSEREF,
    IFREFELSEREF::class to IFREFELSEREF,
    IFBITJMP::class to IFBITJMP,
    IFNBITJMP::class to IFNBITJMP,
    IFBITJMPREF::class to IFBITJMPREF,
    IFNBITJMPREF::class to IFNBITJMPREF,
    REPEAT::class to REPEAT,
    REPEATEND::class to REPEATEND,
    UNTIL::class to UNTIL,
    UNTILEND::class to UNTILEND,
    WHILE::class to WHILE,
    WHILEEND::class to WHILEEND,
    AGAIN::class to AGAIN,
    AGAINEND::class to AGAINEND,
    REPEATBRK::class to REPEATBRK,
    REPEATENDBRK::class to REPEATENDBRK,
    UNTILBRK::class to UNTILBRK,
    UNTILENDBRK::class to UNTILENDBRK,
    WHILEBRK::class to WHILEBRK,
    WHILEENDBRK::class to WHILEENDBRK,
    AGAINBRK::class to AGAINBRK,
    AGAINENDBRK::class to AGAINENDBRK,
    SETCONTARGS_N::class to SETCONTARGS_N,
    SETNUMARGS::class to SETNUMARGS,
    SETCONTARGS::class to SETCONTARGS,
    RETURNARGS::class to RETURNARGS,
    RETURNVARARGS::class to RETURNVARARGS,
    SETCONTVARARGS::class to SETCONTVARARGS,
    SETNUMVARARGS::class to SETNUMVARARGS,
    BLESS::class to BLESS,
    BLESSVARARGS::class to BLESSVARARGS,
    BLESSARGS::class to BLESSARGS,
    BLESSNUMARGS::class to BLESSNUMARGS,
    PUSHCTR::class to PUSHCTR,
    PUSHROOT::class to PUSHROOT,
    POPCTR::class to POPCTR,
    POPROOT::class to POPROOT,
    SETCONTCTR::class to SETCONTCTR,
    SETRETCTR::class to SETRETCTR,
    SETALTCTR::class to SETALTCTR,
    POPSAVE::class to POPSAVE,
    SAVE::class to SAVE,
    SAVEALT::class to SAVEALT,
    SAVEBOTH::class to SAVEBOTH,
    PUSHCTRX::class to PUSHCTRX,
    POPCTRX::class to POPCTRX,
    SETCONTCTRX::class to SETCONTCTRX,
    COMPOS::class to COMPOS,
    COMPOSALT::class to COMPOSALT,
    COMPOSBOTH::class to COMPOSBOTH,
    ATEXIT::class to ATEXIT,
    ATEXITALT::class to ATEXITALT,
    SETEXITALT::class to SETEXITALT,
    THENRET::class to THENRET,
    THENRETALT::class to THENRETALT,
    INVERT::class to INVERT,
    BOOLEVAL::class to BOOLEVAL,
    SAMEALT::class to SAMEALT,
    SAMEALTSAVE::class to SAMEALTSAVE,
    CALLDICT::class to CALLDICT,
    CALLDICT_LONG::class to CALLDICT_LONG,
    JMPDICT::class to JMPDICT,
    PREPAREDICT::class to PREPAREDICT,
    THROW_SHORT::class to THROW_SHORT,
    THROWIF_SHORT::class to THROWIF_SHORT,
    THROWIFNOT_SHORT::class to THROWIFNOT_SHORT,
    THROW::class to THROW,
    THROWARG::class to THROWARG,
    THROWIF::class to THROWIF,
    THROWARGIF::class to THROWARGIF,
    THROWIFNOT::class to THROWIFNOT,
    THROWARGIFNOT::class to THROWARGIFNOT,
    THROWANY::class to THROWANY,
    THROWARGANY::class to THROWARGANY,
    THROWANYIF::class to THROWANYIF,
    THROWARGANYIF::class to THROWARGANYIF,
    THROWANYIFNOT::class to THROWANYIFNOT,
    THROWARGANYIFNOT::class to THROWARGANYIFNOT,
    TRY::class to TRY,
    TRYARGS::class to TRYARGS,
    NEWDICT::class to NEWDICT,
    DICTEMPTY::class to DICTEMPTY,
    STDICTS::class to STDICTS,
    STDICT::class to STDICT,
    SKIPDICT::class to SKIPDICT,
    LDDICTS::class to LDDICTS,
    PLDDICTS::class to PLDDICTS,
    LDDICT::class to LDDICT,
    PLDDICT::class to PLDDICT,
    LDDICTQ::class to LDDICTQ,
    PLDDICTQ::class to PLDDICTQ,
    DICTGET::class to DICTGET,
    DICTGETREF::class to DICTGETREF,
    DICTIGET::class to DICTIGET,
    DICTIGETREF::class to DICTIGETREF,
    DICTUGET::class to DICTUGET,
    DICTUGETREF::class to DICTUGETREF,
    DICTSET::class to DICTSET,
    DICTSETREF::class to DICTSETREF,
    DICTISET::class to DICTISET,
    DICTISETREF::class to DICTISETREF,
    DICTUSET::class to DICTUSET,
    DICTUSETREF::class to DICTUSETREF,
    DICTSETGET::class to DICTSETGET,
    DICTSETGETREF::class to DICTSETGETREF,
    DICTISETGET::class to DICTISETGET,
    DICTISETGETREF::class to DICTISETGETREF,
    DICTUSETGET::class to DICTUSETGET,
    DICTUSETGETREF::class to DICTUSETGETREF,
    DICTREPLACE::class to DICTREPLACE,
    DICTREPLACEREF::class to DICTREPLACEREF,
    DICTIREPLACE::class to DICTIREPLACE,
    DICTIREPLACEREF::class to DICTIREPLACEREF,
    DICTUREPLACE::class to DICTUREPLACE,
    DICTUREPLACEREF::class to DICTUREPLACEREF,
    DICTREPLACEGET::class to DICTREPLACEGET,
    DICTREPLACEGETREF::class to DICTREPLACEGETREF,
    DICTIREPLACEGET::class to DICTIREPLACEGET,
    DICTIREPLACEGETREF::class to DICTIREPLACEGETREF,
    DICTUREPLACEGET::class to DICTUREPLACEGET,
    DICTUREPLACEGETREF::class to DICTUREPLACEGETREF,
    DICTADD::class to DICTADD,
    DICTADDREF::class to DICTADDREF,
    DICTIADD::class to DICTIADD,
    DICTIADDREF::class to DICTIADDREF,
    DICTUADD::class to DICTUADD,
    DICTUADDREF::class to DICTUADDREF,
    DICTADDGET::class to DICTADDGET,
    DICTADDGETREF::class to DICTADDGETREF,
    DICTIADDGET::class to DICTIADDGET,
    DICTIADDGETREF::class to DICTIADDGETREF,
    DICTUADDGET::class to DICTUADDGET,
    DICTUADDGETREF::class to DICTUADDGETREF,
    DICTSETB::class to DICTSETB,
    DICTISETB::class to DICTISETB,
    DICTUSETB::class to DICTUSETB,
    DICTSETGETB::class to DICTSETGETB,
    DICTISETGETB::class to DICTISETGETB,
    DICTUSETGETB::class to DICTUSETGETB,
    DICTREPLACEB::class to DICTREPLACEB,
    DICTIREPLACEB::class to DICTIREPLACEB,
    DICTUREPLACEB::class to DICTUREPLACEB,
    DICTREPLACEGETB::class to DICTREPLACEGETB,
    DICTIREPLACEGETB::class to DICTIREPLACEGETB,
    DICTUREPLACEGETB::class to DICTUREPLACEGETB,
    DICTADDB::class to DICTADDB,
    DICTIADDB::class to DICTIADDB,
    DICTUADDB::class to DICTUADDB,
    DICTADDGETB::class to DICTADDGETB,
    DICTIADDGETB::class to DICTIADDGETB,
    DICTUADDGETB::class to DICTUADDGETB,
    DICTDEL::class to DICTDEL,
    DICTIDEL::class to DICTIDEL,
    DICTUDEL::class to DICTUDEL,
    DICTDELGET::class to DICTDELGET,
    DICTDELGETREF::class to DICTDELGETREF,
    DICTIDELGET::class to DICTIDELGET,
    DICTIDELGETREF::class to DICTIDELGETREF,
    DICTUDELGET::class to DICTUDELGET,
    DICTUDELGETREF::class to DICTUDELGETREF,
    DICTGETOPTREF::class to DICTGETOPTREF,
    DICTIGETOPTREF::class to DICTIGETOPTREF,
    DICTUGETOPTREF::class to DICTUGETOPTREF,
    DICTSETGETOPTREF::class to DICTSETGETOPTREF,
    DICTISETGETOPTREF::class to DICTISETGETOPTREF,
    DICTUSETGETOPTREF::class to DICTUSETGETOPTREF,
    PFXDICTSET::class to PFXDICTSET,
    PFXDICTREPLACE::class to PFXDICTREPLACE,
    PFXDICTADD::class to PFXDICTADD,
    PFXDICTDEL::class to PFXDICTDEL,
    DICTGETNEXT::class to DICTGETNEXT,
    DICTGETNEXTEQ::class to DICTGETNEXTEQ,
    DICTGETPREV::class to DICTGETPREV,
    DICTGETPREVEQ::class to DICTGETPREVEQ,
    DICTIGETNEXT::class to DICTIGETNEXT,
    DICTIGETNEXTEQ::class to DICTIGETNEXTEQ,
    DICTIGETPREV::class to DICTIGETPREV,
    DICTIGETPREVEQ::class to DICTIGETPREVEQ,
    DICTUGETNEXT::class to DICTUGETNEXT,
    DICTUGETNEXTEQ::class to DICTUGETNEXTEQ,
    DICTUGETPREV::class to DICTUGETPREV,
    DICTUGETPREVEQ::class to DICTUGETPREVEQ,
    DICTMIN::class to DICTMIN,
    DICTMINREF::class to DICTMINREF,
    DICTIMIN::class to DICTIMIN,
    DICTIMINREF::class to DICTIMINREF,
    DICTUMIN::class to DICTUMIN,
    DICTUMINREF::class to DICTUMINREF,
    DICTMAX::class to DICTMAX,
    DICTMAXREF::class to DICTMAXREF,
    DICTIMAX::class to DICTIMAX,
    DICTIMAXREF::class to DICTIMAXREF,
    DICTUMAX::class to DICTUMAX,
    DICTUMAXREF::class to DICTUMAXREF,
    DICTREMMIN::class to DICTREMMIN,
    DICTREMMINREF::class to DICTREMMINREF,
    DICTIREMMIN::class to DICTIREMMIN,
    DICTIREMMINREF::class to DICTIREMMINREF,
    DICTUREMMIN::class to DICTUREMMIN,
    DICTUREMMINREF::class to DICTUREMMINREF,
    DICTREMMAX::class to DICTREMMAX,
    DICTREMMAXREF::class to DICTREMMAXREF,
    DICTIREMMAX::class to DICTIREMMAX,
    DICTIREMMAXREF::class to DICTIREMMAXREF,
    DICTUREMMAX::class to DICTUREMMAX,
    DICTUREMMAXREF::class to DICTUREMMAXREF,
    DICTIGETJMP::class to DICTIGETJMP,
    DICTUGETJMP::class to DICTUGETJMP,
    DICTIGETEXEC::class to DICTIGETEXEC,
    DICTUGETEXEC::class to DICTUGETEXEC,
    DICTPUSHCONST::class to DICTPUSHCONST,
    PFXDICTGETQ::class to PFXDICTGETQ,
    PFXDICTGET::class to PFXDICTGET,
    PFXDICTGETJMP::class to PFXDICTGETJMP,
    PFXDICTGETEXEC::class to PFXDICTGETEXEC,
    PFXDICTCONSTGETJMP::class to PFXDICTCONSTGETJMP,
    DICTIGETJMPZ::class to DICTIGETJMPZ,
    DICTUGETJMPZ::class to DICTUGETJMPZ,
    DICTIGETEXECZ::class to DICTIGETEXECZ,
    DICTUGETEXECZ::class to DICTUGETEXECZ,
    SUBDICTGET::class to SUBDICTGET,
    SUBDICTIGET::class to SUBDICTIGET,
    SUBDICTUGET::class to SUBDICTUGET,
    SUBDICTRPGET::class to SUBDICTRPGET,
    SUBDICTIRPGET::class to SUBDICTIRPGET,
    SUBDICTURPGET::class to SUBDICTURPGET,
    ACCEPT::class to ACCEPT,
    SETGASLIMIT::class to SETGASLIMIT,
    COMMIT::class to COMMIT,
    RANDU256::class to RANDU256,
    RAND::class to RAND,
    SETRAND::class to SETRAND,
    ADDRAND::class to ADDRAND,
    GETPARAM::class to GETPARAM,
    NOW::class to NOW,
    BLOCKLT::class to BLOCKLT,
    LTIME::class to LTIME,
    RANDSEED::class to RANDSEED,
    BALANCE::class to BALANCE,
    MYADDR::class to MYADDR,
    CONFIGROOT::class to CONFIGROOT,
    CONFIGDICT::class to CONFIGDICT,
    CONFIGPARAM::class to CONFIGPARAM,
    CONFIGOPTPARAM::class to CONFIGOPTPARAM,
    GETGLOBVAR::class to GETGLOBVAR,
    GETGLOB::class to GETGLOB,
    SETGLOBVAR::class to SETGLOBVAR,
    SETGLOB::class to SETGLOB,
    HASHCU::class to HASHCU,
    HASHSU::class to HASHSU,
    SHA256U::class to SHA256U,
    CHKSIGNU::class to CHKSIGNU,
    CHKSIGNS::class to CHKSIGNS,
    CDATASIZEQ::class to CDATASIZEQ,
    CDATASIZE::class to CDATASIZE,
    SDATASIZEQ::class to SDATASIZEQ,
    SDATASIZE::class to SDATASIZE,
    LDGRAMS::class to LDGRAMS,
    LDVARINT16::class to LDVARINT16,
    STGRAMS::class to STGRAMS,
    STVARINT16::class to STVARINT16,
    LDMSGADDR::class to LDMSGADDR,
    LDMSGADDRQ::class to LDMSGADDRQ,
    PARSEMSGADDR::class to PARSEMSGADDR,
    PARSEMSGADDRQ::class to PARSEMSGADDRQ,
    REWRITESTDADDR::class to REWRITESTDADDR,
    REWRITESTDADDRQ::class to REWRITESTDADDRQ,
    REWRITEVARADDR::class to REWRITEVARADDR,
    REWRITEVARADDRQ::class to REWRITEVARADDRQ,
    SENDRAWMSG::class to SENDRAWMSG,
    RAWRESERVE::class to RAWRESERVE,
    RAWRESERVEX::class to RAWRESERVEX,
    SETCODE::class to SETCODE,
    SETLIBCODE::class to SETLIBCODE,
    CHANGELIB::class to CHANGELIB,
    DEBUG::class to DEBUG,
    DEBUGSTR::class to DEBUGSTR,
    DUMPSTK::class to DUMPSTK,
    DUMP::class to DUMP,
    SETCP::class to SETCP,
    SETCP0::class to SETCP0,
    SETCP_SPECIAL::class to SETCP_SPECIAL,
    SETCPX::class to SETCPX,
)