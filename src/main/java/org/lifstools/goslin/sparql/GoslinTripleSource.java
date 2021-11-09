package org.lifstools.goslin.sparql;

import java.util.Optional;
import org.eclipse.rdf4j.common.iteration.CloseableIteration;
import org.eclipse.rdf4j.common.iteration.EmptyIteration;
import org.eclipse.rdf4j.common.iteration.SingletonIteration;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.query.QueryEvaluationException;
import org.eclipse.rdf4j.query.algebra.evaluation.TripleSource;
import org.lifstools.jgoslin.domain.LipidAdduct;
import org.lifstools.jgoslin.domain.LipidLevel;
import org.lifstools.jgoslin.parser.GoslinParser;
import org.lifstools.jgoslin.parser.HmdbParser;
import org.lifstools.jgoslin.parser.LipidMapsParser;
import org.lifstools.jgoslin.parser.LipidParser;
import org.lifstools.jgoslin.parser.Parser;
import org.lifstools.jgoslin.parser.ShorthandParser;
import org.lifstools.jgoslin.parser.SwissLipidsParser;

public class GoslinTripleSource implements TripleSource {

    private final ValueFactory vf;

    public GoslinTripleSource(ValueFactory vf) {
        this.vf = vf;
    }

    @Override
    public CloseableIteration<? extends Statement, QueryEvaluationException> getStatements(Resource subject,
            IRI predicate, Value object, Resource... contexts) throws QueryEvaluationException {
        if (predicate == null) {
            return new EmptyIteration<>();
        }

        Optional<GoslinPredicate> gi = GoslinPredicate.forLocalName(predicate.getLocalName());
        if (gi.isEmpty()) {
            // unknown predicate
            return new EmptyIteration<>();
        }
        // TODO Here the main work needs to be done.
        // We should define a set of predicates that allow us to select a parser.
        // Assuming an input query
        // PREFIX goslin: <TO BE DEFINED>
        // SELECT ?string
        // WHERE { [] goslin:swisslipids 'Cer(d18:1/20:2)' ; 
        //            goslin:lipidClassName ?string . }
        switch (gi.get()) {
            case SWISS_LIPIDS -> {
                return handleParser(SwissLipidsParser.newInstance(), predicate, object);
            }
            case LIPID_MAPS -> {
                return handleParser(LipidMapsParser.newInstance(), predicate, object);
            }
            case GOSLIN -> {
                return handleParser(GoslinParser.newInstance(), predicate, object);
            }
            case SHORTHAND_2020 -> {
                return handleParser(ShorthandParser.newInstance(), predicate, object);
            }
            case HMDB -> {
                return handleParser(HmdbParser.newInstance(), predicate, object);
            }
            case ANY_GRAMMAR -> {
                return handleAnyGrammar(predicate, object);
            }
            case LIPID_MW -> {
                if (subject.isBNode()) {
                    LipidAdduct lipid = ((LipidWrappingBnode) subject).getLipidAdduct();
                    final Literal mw = vf.createLiteral(lipid.getMass());
                    if (object == null || mw.equals(object)) {
                        Statement stat = vf.createStatement(subject, predicate, mw);
                        return new SingletonIteration<>(stat);
                    } else {
                        return new EmptyIteration<>();
                    }
                }
                return new EmptyIteration<>();
            }
            case LIPID_SUM_FORMULA -> {
                if (subject.isBNode()) {
                    LipidAdduct lipid = ((LipidWrappingBnode) subject).getLipidAdduct();
                    final Literal sf = vf.createLiteral(lipid.getSumFormula());
                    if (object == null || sf.equals(object)) {
                        Statement stat = vf.createStatement(subject, predicate, sf);
                        return new SingletonIteration<>(stat);
                    } else {
                        return new EmptyIteration<>();
                    }
                }
                return new EmptyIteration<>();
            }
            case LIPID_PARENT_CLASSES -> {
            }
            case LIPID_CLASS_NAME -> {
                if (subject.isBNode()) {
                    LipidAdduct lipid = ((LipidWrappingBnode) subject).getLipidAdduct();
                    final Literal lipidClassName = vf.createLiteral(lipid.getLipidString(LipidLevel.CLASS));
                    if (object == null || lipidClassName.equals(object)) {
                        Statement stat = vf.createStatement(subject, predicate, lipidClassName);
                        return new SingletonIteration<>(stat);
                    } else {
                        return new EmptyIteration<>();
                    }
                }
                return new EmptyIteration<>();
            }
        }
        return new EmptyIteration<>();
    }

    @Override
    public ValueFactory getValueFactory() {
        return vf;
    }

    private CloseableIteration<? extends Statement, QueryEvaluationException> handleParser(Parser<LipidAdduct> parser, IRI predicate, Value object) {
        if (parser != null && object == null) {
            //We have a parser but no string to parse
            return new EmptyIteration<>();
        } else if (parser != null && object.isLiteral() && ((Literal) object).getDatatype().equals(XSD.STRING)) {
            Optional<LipidAdduct> optLipid = Optional.ofNullable(parser.parse(object.stringValue(), parser.newEventHandler(), false));
            return handleOptionalLipidAdduct(optLipid, predicate, object);
        } else {
            //The object is not a string so we can't parse it
            return new EmptyIteration<>();
        }
    }

    private CloseableIteration<? extends Statement, QueryEvaluationException> handleOptionalLipidAdduct(Optional<LipidAdduct> optLipid, IRI predicate, Value object) {
        if (optLipid.isPresent()) {
            // We are going to hide this in a bnode for use later
            Resource subject = new LipidWrappingBnode(optLipid.get());
            Statement stat = vf.createStatement(subject, predicate, object);
            return new SingletonIteration<>(stat);
        }
        //The object is not a string so we can't parse it
        return new EmptyIteration<>();
    }

    private CloseableIteration<? extends Statement, QueryEvaluationException> handleAnyGrammar(IRI predicate, Value object) {
        if (predicate != null && object != null && object.isLiteral() && ((Literal) object).getDatatype().equals(XSD.STRING)) {
            LipidParser p = new LipidParser();
            Optional<LipidAdduct> optLipid = Optional.ofNullable(p.parse(object.stringValue()));
            return handleOptionalLipidAdduct(optLipid, predicate, object);
        } else {
            return new EmptyIteration<>();
        }
    }

}
