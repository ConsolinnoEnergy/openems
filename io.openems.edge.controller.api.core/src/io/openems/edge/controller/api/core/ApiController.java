package io.openems.edge.controller.api.core;

import java.util.List;

import org.osgi.service.cm.ConfigurationAdmin;

import io.openems.edge.common.component.OpenemsComponent;
import io.openems.edge.controller.api.Controller;
import io.openems.edge.provisioning.api.Provisioning;
import io.openems.edge.timedata.api.Timedata;

public interface ApiController extends Controller {

	Timedata getTimedataService();

	List<OpenemsComponent> getComponents();

	ConfigurationAdmin getConfigurationAdmin();

	List<Provisioning> getProvisionings();
}
