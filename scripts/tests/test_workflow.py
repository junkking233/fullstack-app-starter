import copy
import importlib.util
import unittest
from pathlib import Path


SCRIPT_PATH = Path(__file__).resolve().parents[1] / "workflow.py"
SPEC = importlib.util.spec_from_file_location("workflow_script", SCRIPT_PATH)
WORKFLOW = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
SPEC.loader.exec_module(WORKFLOW)


class WorkflowValidationTest(unittest.TestCase):
    def setUp(self):
        self.state = WORKFLOW.load_state()

    def test_template_baseline_is_valid(self):
        self.assertEqual([], WORKFLOW.validate_state(copy.deepcopy(self.state)))

    def test_future_pending_gates_do_not_block_current_stage(self):
        state = copy.deepcopy(self.state)
        self.assertTrue(all(gate["status"] == "pending" for gate in state["workflow"]["gates"]["5"]))
        self.assertEqual([], WORKFLOW.validate_state(state))

    def test_template_cleanup_is_required_before_implementation(self):
        gates = self.state["workflow"]["gates"]
        self.assertNotIn("template_cleanup", [gate["id"] for gate in gates["0"]])
        self.assertIn("template_cleanup", [gate["id"] for gate in gates["3"]])

    def test_blocked_stage_requires_blocker_and_resume_action(self):
        state = copy.deepcopy(self.state)
        state["workflow"]["stages"][0]["status"] = "blocked"
        state["workflow"]["currentStageStatus"] = "blocked"
        errors = WORKFLOW.validate_state(state)
        self.assertTrue(any("必须记录 blocker" in error for error in errors))

        state["workflow"]["blockers"] = [{"id": "B1", "message": "缺少设计确认", "resumeAction": "请用户确认"}]
        errors = WORKFLOW.validate_state(state)
        self.assertFalse(any("blocker" in error for error in errors))

    def test_completed_gate_requires_evidence(self):
        state = copy.deepcopy(self.state)
        state["workflow"]["gates"]["0"][0]["status"] = "completed"
        errors = WORKFLOW.validate_state(state)
        self.assertTrue(any("完成但缺少证据" in error for error in errors))


if __name__ == "__main__":
    unittest.main()
